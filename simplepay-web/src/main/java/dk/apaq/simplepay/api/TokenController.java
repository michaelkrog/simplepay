package dk.apaq.simplepay.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.data.ITransactionRepository;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.quickpay.QuickPay;
import dk.apaq.simplepay.model.*;
import dk.apaq.simplepay.security.SecurityHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author krog
 */
@Controller
public class TokenController {

    private static final Logger LOG = LoggerFactory.getLogger(TokenController.class);
    public static final String[] QUICKPAY_KEYS = {"msgtype", "ordernumber", "amount", "currency", "time", "state", "qpstat", "qpstatmsg",
        "chstat", "chstatmsg", "merchant", "merchantemail", "transaction", "cardtype", "cardnumber",
        "splitpayment", "fraudprobability", "fraudremarks", "fraudreport", "fee"};
    public static final String[] QUICKPAY_KEYS_SUBSCRIBE = {"msgtype", "ordernumber", "amount", "currency", "time", "state", "qpstat", "qpstatmsg",
        "chstat", "chstatmsg", "merchant", "merchantemail", "transaction", "cardtype", "cardnumber",
        "cardexpire", "splitpayment", "fraudprobability", "fraudremarks", "fraudreport", "fee"};
    @Autowired
    private IPayService service;
    @Autowired
    private PaymentGatewayManager gatewayManager;
    @Autowired
    @Qualifier("publicUrl")
    private String publicUrl;

    private Token getToken(Merchant m, String token) {
        Token t = service.getTokens(m).findOne(token);
        if (t == null) {
            throw new ResourceNotFoundException("No token exists with the given token id.");
        }
        return t;
    }

    @RequestMapping(value = "/tokens", method = RequestMethod.POST)
    @Transactional()
    @Secured({"ROLE_PUBLICAPIACCESSOR", "ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public String createToken(HttpServletRequest request, @RequestParam String cardNumber, @RequestParam int expireMonth, @RequestParam int expireYear, @RequestParam String cvd) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Creating token. [merchant={}]", m.getId());

        Card card = new Card(cardNumber, expireMonth, expireYear, cvd);
        return service.getTokens(m).createNew(card).getId();
    }

    @RequestMapping(value = "/tokens", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public List<Token> listTokens() {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Listing tokens. [merchant={}]", m.getId());
        return service.getTokens(m).findAll();
    }

    @RequestMapping(value = "/tokens/{token}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public Token getToken(@PathVariable String token) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Retrieving token. [merchant={};token={}]", m.getId(), token);
        return getToken(m, token);
    }

    @RequestMapping(value = "/callback/quickpay/{publicKey}/{tokenId}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void handleQuickpayEvent(MultipartHttpServletRequest request, @PathVariable String publicKey, @PathVariable String tokenId) {
        LOG.debug("Payment event recieved");

        SystemUser user = service.getUser(publicKey);
        if (user == null) {
            throw new ResourceNotFoundException("No user with the given key was found");
        }
        Merchant merchant = user.getMerchant();
        ITransactionRepository transactions = service.getTransactions(merchant);
        String eventType = request.getParameter("msgtype");
        String qpstat = request.getParameter("qpstat");
        String qpstatmsg = request.getParameter("qpstatmsg");
        String currency = request.getParameter("currency");
        String gatewayTransactionId = request.getParameter("transaction");
        String orderNumber = request.getParameter("ordernumber");

        String[] keys = "subscribe".equals(eventType) ? QUICKPAY_KEYS_SUBSCRIBE : QUICKPAY_KEYS;
        String secret = null;

        for (PaymentGatewayAccess pga : merchant.getPaymentGatewayAccesses()) {
            if (pga.getPaymentGatewayType() == EPaymentGateway.QuickPay) {
                secret = pga.getAcquirerApiKey();
                break;
            }
        }

        if (!validateQuickpayCallback(request, request.getParameter("md5check"), secret, keys)) {
            LOG.warn("Payment data is invalid!!! [merchantId={}; remoteIp={}]", merchant.getId(), request.getRemoteAddr());
            throw new InvalidRequestException("The data sent is not valid(checked against md5check).");
        }

        try {
            QuickPay.checkQuickpayResult(qpstat, qpstatmsg);
        } catch (PaymentException ex) {
            LOG.debug("Payment did not have a qpstat we handle. [qpstat={}]", qpstat);
            //What to do about the exception?
        }


        long amount;

        LOG.debug("Payment event type is " + eventType);

        try {
            amount = Long.parseLong(request.getParameter("amount"));
        } catch (NumberFormatException ex) {
            LOG.warn("Payment data was valid, but amount is not a valid number!!! [amount={}; remoteIp={}]", request.getParameter("amount"), request.getRemoteAddr());
            throw new InvalidRequestException("amount not a valid number [value=" + request.getParameter("amount") + "]");
        }

        Token token = service.getTokens(merchant).findOne(tokenId);
        Transaction transaction = service.getTransactionByRefId(merchant, orderNumber);

        if (transaction == null) {
            throw new ResourceNotFoundException("Could not find the transaction.");
        }

        if (!orderNumber.equals(transaction.getRefId())) {
            throw new InvalidRequestException("Ordernumber does not match.");
        }

        int expireMonth = 0;
        int expireYear = 0;

        if (request.getParameter("cardexpire") != null) {
            String expireTime = request.getParameter("cardexpire");
            expireMonth = Integer.parseInt(expireTime.substring(0, 2));
            expireYear = Integer.parseInt(expireTime.substring(2, 4));
        }

        /*if ("authorize".equals(eventType)) {
         EPaymentMethod paymentMethod = QuickPay.getCardTypeFromString(request.getParameter("cardtype"));
         token = service.getTokens(merchant).authorizedRemote(token, currency, amount, paymentMethod, expireMonth, expireYear, 
         request.getParameter("cardnumber"), gatewayTransactionId);
         service.getTransactions(merchant).createNew(token);

         }*/
    }

    private boolean validateQuickpayCallback(HttpServletRequest request, String md5check, String secret, String[] keys) {
        assert md5check != null;
        assert secret != null;
        assert request != null;
        assert keys != null;

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(request.getParameter(key));
        }
        sb.append(secret);
        return md5check.equals(DigestUtils.md5Hex(sb.toString()));
    }
}

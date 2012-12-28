
package dk.apaq.simplepay.api;

import javax.servlet.http.HttpServletRequest;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.data.ITransactionRepository;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.quickpay.QuickPay;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * Javadoc
 */
@Controller
public class CallbackController {

    public static final String[] QUICKPAY_KEYS = {"msgtype", "ordernumber", "amount", "currency", "time", "state", "qpstat", "qpstatmsg",
        "chstat", "chstatmsg", "merchant", "merchantemail", "transaction", "cardtype", "cardnumber",
        "splitpayment", "fraudprobability", "fraudremarks", "fraudreport", "fee"};
    public static final String[] QUICKPAY_KEYS_SUBSCRIBE = {"msgtype", "ordernumber", "amount", "currency", "time", "state", "qpstat", "qpstatmsg",
        "chstat", "chstatmsg", "merchant", "merchantemail", "transaction", "cardtype", "cardnumber",
        "cardexpire", "splitpayment", "fraudprobability", "fraudremarks", "fraudreport", "fee"};
    
    private static final Logger LOG = LoggerFactory.getLogger(CallbackController.class);
    @Autowired
    private IPayService service;
    
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

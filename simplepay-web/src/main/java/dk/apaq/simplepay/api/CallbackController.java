package dk.apaq.simplepay.api;

import dk.apaq.crud.Crud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.quickpay.QuickPay;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionStatus;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author krog
 */
@Controller
public class CallbackController {
    
    private static final Logger LOG = LoggerFactory.getLogger(CallbackController.class);
    
    public static final String[] QUICKPAY_KEYS = {"msgtype", "ordernumber", "amount", "currency", "time", "state", "qpstat", "qpstatmsg", 
                        "chstat", "chstatmsg", "merchant", "merchantemail", "transaction", "cardtype", "cardnumber",
                        "splitpayment", "fraudprobability", "fraudremarks", "fraudreport", "fee"};
    public static final String[] QUICKPAY_KEYS_SUBSCRIBE = {"msgtype", "ordernumber", "amount", "currency", "time", "state", "qpstat", "qpstatmsg", 
                        "chstat", "chstatmsg", "merchant", "merchantemail", "transaction", "cardtype", "cardnumber",
                        "cardexpire", "splitpayment", "fraudprobability", "fraudremarks", "fraudreport", "fee"};
    
    @Autowired
    private PayService service;
    
    @Autowired
    private PaymentGatewayManager gatewayManager;
    
    @RequestMapping(value="/callback/quickpay/{publicKey}/{token}", method=RequestMethod.POST) 
    public void handleQuickpayEvent(MultipartHttpServletRequest request, @PathVariable String publicKey, @PathVariable String token) {
        LOG.debug("Payment event recieved");
        
        Merchant merchant = service.getMerchantByPublicKey(publicKey);
        Crud.Complete<String, Transaction> transactions = service.getTransactions(merchant);
        String eventType = request.getParameter("msgtype");
        String qpstat = request.getParameter("qpstat");
        String qpstatmsg = request.getParameter("qpstatmsg");
        String currency = request.getParameter("currency");
        String gatewayTransactionId = request.getParameter("transaction");
        
        
        String[] keys = "subscribe".equals(eventType) ? QUICKPAY_KEYS_SUBSCRIBE : QUICKPAY_KEYS;
        
        if(!validate(request, request.getParameter("md5check"), merchant.getGatewaySecret(), keys)) {
            LOG.warn("Payment data is invalid!!! [merchantId={}; remoteIp={}]", merchant.getId(), request.getRemoteAddr());
            throw new InvalidRequestException("The data sent is not valid(checked against md5check).");
        }
        
        try {
            QuickPay.checkQuickpayResult(qpstat, qpstatmsg);
        } catch(PaymentException ex) {
            LOG.debug("Payment did not have a qpstat we handle. [qpstat={}]", qpstat);
            //What to do about the exception?
        }                                      

        
        long orderNumber;
        long amount;
        
        LOG.debug("Payment event type is " + eventType);
        
        try {
            orderNumber = Long.parseLong(request.getParameter("ordernumber"));
        } catch(NumberFormatException ex) {
            LOG.warn("Payment data was valid, but orderNumber is not a valid number!!! [ordernumber{}; remoteIp={}]", request.getParameter("ordernumber"), request.getRemoteAddr());
            throw new InvalidRequestException("ordernumber not a valid number [value="+request.getParameter("ordernumber") +"]");
        }
        
        try {
            amount = Long.parseLong(request.getParameter("amount"));
        } catch(NumberFormatException ex) {
            LOG.warn("Payment data was valid, but amount is not a valid number!!! [amount={}; remoteIp={}]", request.getParameter("amount"), request.getRemoteAddr());
            throw new InvalidRequestException("amount not a valid number [value="+request.getParameter("amount") +"]");
        }
        
        if("authorize".equals(eventType)) {
        
            //Find transaction from ordernumber
            Filter filter = new CompareFilter("orderNumber", orderNumber, CompareFilter.CompareType.Equals);
            List<String> idlist = service.getTransactions(merchant).listIds(filter, null);
            if(idlist.isEmpty()) {
                throw new ResourceNotFoundException("Could not find the order requested.");
            }
            
            Transaction transaction = transactions.read(idlist.get(0));
            
            if(!currency.equals(transaction.getCurrency())) {
                throw new InvalidRequestException("Currency does not match what originally was requested.");
            }
            
            //markér som authorized med den givne amount og sæt andre info
            transaction.setAuthorizedAmount(amount);
            transaction.setStatus(TransactionStatus.Authorized);
            transaction.setGatewayTransactionId(gatewayTransactionId);
            transaction.setCardType(QuickPay.getCardTypeFromString(request.getParameter("cardtype")));
            transaction.setCardExpires(request.getParameter("cardexpire"));
            transaction.setCardNumberTruncated(request.getParameter("cardnumber"));
            transactions.update(transaction);
            
        }
    }
    
    private boolean validate(HttpServletRequest request, String md5check, String secret, String[] keys) {
        assert md5check != null;
        assert secret != null;
        assert request != null;
        assert keys != null;
        
        StringBuilder sb = new StringBuilder();
        for(String key : keys) {
            sb.append(request.getParameter(key));
        }
        sb.append(secret);
        return md5check.equals(DigestUtils.md5Hex(sb.toString()));
    }
}

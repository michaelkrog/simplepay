package dk.apaq.simplepay.api;

import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionStatus;
import dk.apaq.simplepay.security.MerchantUserDetailsHolder;
import java.text.NumberFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author krog
 */
@Controller
public class TransactionController {
    
    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);
    private static final NumberFormat nfQuickPayOrderNumber = NumberFormat.getIntegerInstance();
    
    static {
        nfQuickPayOrderNumber.setGroupingUsed(false);
    }
    
    
    @Autowired
    private PayService service;
    
    @Autowired
    private PaymentGatewayManager gatewayManager;
    
    @Autowired
    @Qualifier("publicUrl")
    private String publicUrl;
    
    public class FormData {
        private String url;
        private Map<String, String> fields = new LinkedHashMap<String, String>();

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, String> getFields() {
            return fields;
        }
        
    }
    
    
    private Merchant getMerchant() {
        return MerchantUserDetailsHolder.getMerchantUserDetails().getMerchant();
    }
    
    private Transaction getTransaction(Merchant m, String token) {
        Transaction t = service.getTransactions(m).read(token);
        if(t == null) {
            throw new ResourceNotFoundException("No transaction exists with the given token.");
        }
        return t;
    }
    
    
    @RequestMapping(value = "/form", method=RequestMethod.POST)
    @Secured({"ROLE_PUBLIC","ROLE_PRIVATE"})
    @ResponseBody
    public FormData generateForm(HttpServletRequest request, String token, Long amount, String currency, String returnUrl, String cancelUrl) {
        Merchant m = getMerchant();
        Transaction t = getTransaction(token);
        
        t.setCurrency(currency);
        service.getTransactions(m).update(t);
        
        String callbackUrl = publicUrl + "/api/callback/quickpay/" + m.getPublicKey() + "/" + token;
        
        FormData formData = new FormData();
        formData.setUrl("https://secure.quickpay.dk/form/");
        
        Map<String, String> map = formData.getFields();
        map.put("protocol", "4");
        map.put("msgtype", "authorize");
        map.put("merchant", m.getGatewayUserId());
        map.put("language", request.getLocale().getLanguage());
        map.put("ordernumber", nfQuickPayOrderNumber.format(t.getOrderNumber()));  //
        map.put("amount", Long.toString(amount));
        map.put("currency", currency);
        map.put("continueurl", returnUrl);
        map.put("cancelurl", cancelUrl);
        map.put("callbackurl", callbackUrl);
        map.put("autocapture", "0");
        map.put("cardtypelock", "creditcard");
        map.put("splitpayment", "1");
        
        //md5
        StringBuilder builder = new StringBuilder();
        for(String value : map.values()) {
            builder.append(value);
        }
        builder.append(m.getGatewaySecret());
        map.put("md5check", DigestUtils.md5Hex(builder.toString()));
        
        return formData;
    }

    @RequestMapping(value = "/transactions", method=RequestMethod.POST)
    @Transactional(readOnly=true)
    @Secured({"ROLE_PUBLIC","ROLE_PRIVATE"})
    @ResponseBody
    public String createTransactions(@RequestParam Long orderNumber, @RequestParam String description) {
        Merchant m = getMerchant();
        LOG.debug("Creating transaction. [merchant={}; orderNumber={}]", m.getId(), orderNumber);
        
        Transaction transaction = new Transaction();
        transaction.setOrderNumber(orderNumber);
        transaction.setDescription(description);
        transaction = service.getTransactions(m).createAndRead(transaction);
        return transaction.getId();
        
    }
    
    
    @RequestMapping(value = "/transactions" , method=RequestMethod.GET)
    @Transactional(readOnly=true)
    @Secured("ROLE_PRIVATE")
    @ResponseBody
    public List<Transaction> listTransactions() {
        Merchant m = getMerchant();
        LOG.debug("Listing transactions. [merchant={}]", m.getId());
        return service.getTransactions(m).list();
    }
    
    @RequestMapping(value="/transactions/{token}", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    @Secured("ROLE_PRIVATE")
    @ResponseBody
    public Transaction getTransaction(@PathVariable String token) {
        Merchant m = getMerchant();
        LOG.debug("Retrieving transaction. [merchant={};token={}]", m.getId(), token);
        return getTransaction(m, token);
    }
    
    @RequestMapping(value="/transactions/{token}/refund", method=RequestMethod.POST)
    @Transactional
    @Secured("ROLE_PRIVATE")
    @ResponseBody
    public Transaction refundTransaction(@PathVariable String token, @RequestParam Long amount) {
        Merchant m = getMerchant();
        LOG.debug("Refunding transaction. [merchant={}; token={}; amount={}]", new Object[]{m.getId(), token, amount});
        Transaction t = getTransaction(m, token);
        
        if(amount == null) {
            amount = t.getAuthorizedAmount();
        }
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(t.getGatewayType(), m.getGatewayUserId(), m.getGatewaySecret());
        gateway.refund(amount, t.getGatewayTransactionId());
        t.setStatus(TransactionStatus.Refunded);
        t.setRefundedAmount(amount);
        return service.getTransactions(m).update(t);
    }
    
    @RequestMapping(value="/transactions/{token}/charge", method=RequestMethod.POST)
    @Transactional
    @Secured("ROLE_PRIVATE")
    @ResponseBody
    public Transaction chargeTransaction(@PathVariable String token, @RequestParam Long amount) {
        Merchant m = getMerchant();
        LOG.debug("Charging transaction. [merchant={}; token={}; amount={}]", new Object[]{m.getId(), token, amount});
        Transaction t = getTransaction(m, token);
        
        if(amount == null) {
            amount = t.getAuthorizedAmount();
        }
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(t.getGatewayType(), m.getGatewayUserId(), m.getGatewaySecret());
        gateway.capture(amount, t.getGatewayTransactionId());
        t.setStatus(TransactionStatus.Captured);
        t.setCapturedAmount(amount);
        return service.getTransactions(m).update(t);
    }
    
    @RequestMapping(value="/transactions/{token}/cancel", method=RequestMethod.POST)
    @Transactional
    @Secured("ROLE_PRIVATE")
    @ResponseBody
    public Transaction cancelTransaction(@PathVariable String token) {
        Merchant m = getMerchant();
        LOG.debug("Cancelling transaction. [merchant={}; token={}]", m.getId(), token);
        Transaction t = getTransaction(m, token);
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(t.getGatewayType(), m.getGatewayUserId(), m.getGatewaySecret());
        gateway.cancel(t.getGatewayTransactionId());
        t.setStatus(TransactionStatus.Cancelled);
        return service.getTransactions(m).update(t);
    }
}

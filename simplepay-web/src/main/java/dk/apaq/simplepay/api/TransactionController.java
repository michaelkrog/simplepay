package dk.apaq.simplepay.api;

import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionStatus;
import dk.apaq.simplepay.security.MerchantUserDetails;
import dk.apaq.simplepay.security.MerchantUserDetailsHolder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author krog
 */
@Controller
public class TransactionController {
    
    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);
    
    @Autowired
    private PayService service;
    
    @Autowired
    private PaymentGatewayManager gatewayManager;
    
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
    
    @RequestMapping(value = "/", method=RequestMethod.POST)
    @Transactional(readOnly=true)
    public String createTransactions(@RequestParam Long orderNumber, String description) {
        Merchant m = getMerchant();
        
        Transaction transaction = new Transaction();
        transaction.setOrderNumber(orderNumber);
        transaction.setDescription(description);
        transaction = service.getTransactions(m).createAndRead(transaction);
        return transaction.getId();
        
    }
    
    
    @RequestMapping(value = "/" , method=RequestMethod.GET)
    @Transactional(readOnly=true)
    public List<String> listTransactions() {
        Merchant m = getMerchant();
        return service.getTransactions(m).listIds();
    }
    
    @RequestMapping(value="/{token}")
    @Transactional(readOnly=true)
    public Transaction getTransaction(@PathVariable String token) {
        Merchant m = getMerchant();
        return getTransaction(m, token);
    }
    
    @RequestMapping(value="/{token}/refund")
    @Transactional
    public Transaction refundTransaction(@PathVariable String token, @RequestParam Long amount) {
        Merchant m = getMerchant();
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
    
    @RequestMapping(value="/{token}/charge")
    @Transactional
    public Transaction chargeTransaction(@PathVariable String token, @RequestParam Long amount) {
        Merchant m = getMerchant();
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
    
    @RequestMapping(value="/{token}/cancel")
    @Transactional
    public Transaction cancelTransaction(@PathVariable String token) {
        Merchant m = getMerchant();
        Transaction t = getTransaction(m, token);
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(t.getGatewayType(), m.getGatewayUserId(), m.getGatewaySecret());
        gateway.cancel(t.getGatewayTransactionId());
        t.setStatus(TransactionStatus.Cancelled);
        return service.getTransactions(m).update(t);
    }
}

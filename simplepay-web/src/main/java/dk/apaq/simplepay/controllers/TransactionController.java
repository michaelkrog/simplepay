package dk.apaq.simplepay.controllers;

import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
    
    private Merchant getMerchant(String secretKey) {
        Merchant m = service.getMerchantBySecretKey(secretKey);
        if(m == null) {
            throw new UnauthorizedException("secretKey given is not at valid key.");
        }
        return m;
    }
    
    private Transaction getTransaction(Merchant m, String token) {
        Transaction t = service.getTransactions(m).read(token);
        if(t == null) {
            throw new ResourceNotFoundException("No transaction exists with the given token.");
        }
        return t;
    }
    
    @RequestMapping(value = "/transactions" , method= RequestMethod.GET)
    public List<String> listTransactions(@RequestHeader String secretKey) {
        Merchant m = getMerchant(secretKey);
        return service.getTransactions(m).listIds();
    }
    
    @RequestMapping(value="/transactions/{token}")
    public Transaction getTransaction(@RequestHeader String secretKey, @PathVariable String token) {
        Merchant m = getMerchant(secretKey);
        return getTransaction(m, token);
    }
    
    @RequestMapping(value="/transactions/{token}/refund")
    public Transaction refundTransaction(@RequestHeader String secretKey, @PathVariable String token, @RequestParam Long amount) {
        Merchant m = getMerchant(secretKey);
        Transaction t = getTransaction(m, token);
        
        if(amount == null) {
            amount = t.getAuthorizedAmount();
        }
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(t.getGatewayType(), m.getGatewayUserId(), m.getGatewaySecret());
        try{
            gateway.refund(amount, t.getGatewayTransactionId());
        } catch(PaymentException ex) {
            LOG.error("Error while refunding via gateway." ,ex);
            throw new RequestFailed("Error while refunding. " + ex.getMessage());
        }
        t.setRefunded(true);
        t.setRefundedAmount(amount);
        return service.getTransactions(m).update(t);
    }
    
    @RequestMapping(value="/transactions/{token}/charge")
    public Transaction chargeTransaction(@RequestHeader String secretKey, @PathVariable String token, @RequestParam Long amount) {
        Merchant m = getMerchant(secretKey);
        Transaction t = getTransaction(m, token);
        
        if(amount == null) {
            amount = t.getAuthorizedAmount();
        }
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(t.getGatewayType(), m.getGatewayUserId(), m.getGatewaySecret());
        try{
            gateway.capture(amount, t.getGatewayTransactionId());
        } catch(PaymentException ex) {
            LOG.error("Error while capturing via gateway." ,ex);
            throw new RequestFailed("Error while charging money. " + ex.getMessage());
        }
        t.setCaptured(true);
        t.setCapturedAmount(amount);
        return service.getTransactions(m).update(t);
    }
    
    @RequestMapping(value="/transactions/{token}/cancel")
    public Transaction cancelTransaction(@RequestHeader String secretKey, @PathVariable String token) {
                Merchant m = getMerchant(secretKey);
        Transaction t = getTransaction(m, token);
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(t.getGatewayType(), m.getGatewayUserId(), m.getGatewaySecret());
        try{
            gateway.cancel(t.getGatewayTransactionId());
        } catch(PaymentException ex) {
            LOG.error("Error while cancelling via gateway." ,ex);
            throw new RequestFailed("Error while cancelling. " + ex.getMessage());
        }
        t.setCancelled(true);
        return service.getTransactions(m).update(t);
    }
}

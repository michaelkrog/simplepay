package dk.apaq.simplepay.controllers;

import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.security.MerchantUserDetails;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    
    private Merchant getMerchant() {
        MerchantUserDetails mud = (MerchantUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mud.getMerchant();
    }
    
    private Transaction getTransaction(Merchant m, String token) {
        Transaction t = service.getTransactions(m).read(token);
        if(t == null) {
            throw new ResourceNotFoundException("No transaction exists with the given token.");
        }
        return t;
    }
    
    @RequestMapping(value = "/transactions" , method= RequestMethod.GET)
    @Transactional(readOnly=true)
    public List<String> listTransactions() {
        Merchant m = getMerchant();
        return service.getTransactions(m).listIds();
    }
    
    @RequestMapping(value="/transactions/{token}")
    @Transactional(readOnly=true)
    public Transaction getTransaction(@PathVariable String token) {
        Merchant m = getMerchant();
        return getTransaction(m, token);
    }
    
    @RequestMapping(value="/transactions/{token}/refund")
    @Transactional
    public Transaction refundTransaction(@PathVariable String token, @RequestParam Long amount) {
        Merchant m = getMerchant();
        Transaction t = getTransaction(m, token);
        
        if(amount == null) {
            amount = t.getAuthorizedAmount();
        }
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(t.getGatewayType(), m.getGatewayUserId(), m.getGatewaySecret());
        gateway.refund(amount, t.getGatewayTransactionId());
        t.setRefunded(true);
        t.setRefundedAmount(amount);
        return service.getTransactions(m).update(t);
    }
    
    @RequestMapping(value="/transactions/{token}/charge")
    @Transactional
    public Transaction chargeTransaction(@PathVariable String token, @RequestParam Long amount) {
        Merchant m = getMerchant();
        Transaction t = getTransaction(m, token);
        
        if(amount == null) {
            amount = t.getAuthorizedAmount();
        }
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(t.getGatewayType(), m.getGatewayUserId(), m.getGatewaySecret());
        gateway.capture(amount, t.getGatewayTransactionId());
        t.setCaptured(true);
        t.setCapturedAmount(amount);
        return service.getTransactions(m).update(t);
    }
    
    @RequestMapping(value="/transactions/{token}/cancel")
    @Transactional
    public Transaction cancelTransaction(@PathVariable String token) {
        Merchant m = getMerchant();
        Transaction t = getTransaction(m, token);
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(t.getGatewayType(), m.getGatewayUserId(), m.getGatewaySecret());
        gateway.cancel(t.getGatewayTransactionId());
        t.setCancelled(true);
        return service.getTransactions(m).update(t);
    }
}

package dk.apaq.simplepay;


import dk.apaq.simplepay.service.EventService;
import dk.apaq.simplepay.service.MerchantService;
import dk.apaq.simplepay.service.TokenService;
import dk.apaq.simplepay.service.TransactionService;
import dk.apaq.simplepay.service.UserService;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of the IPayService interface.<br><br>
 * This implemenation expects a Spring <code>ApplicationConext</code> to be injected via the <code>setApplicationContext</code> method 
 * before being used. 
 */
public class PaymentContext  {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentContext.class);
    
    private MerchantService merchantService;
    private UserService userService;
    private TokenService tokenService;
    private TransactionService transactionService;
    private EventService eventService;
    private PaymentGatewayManager paymentGatewayManager;

    public void setPaymentGatewayManager(PaymentGatewayManager paymentGatewayManager) {
        this.paymentGatewayManager = paymentGatewayManager;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setMerchantService(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    public TokenService getTokenService() {
        return tokenService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public EventService getEventService() {
        return eventService;
    }

    public MerchantService getMerchantService() {
        return merchantService;
    }
    

    
}

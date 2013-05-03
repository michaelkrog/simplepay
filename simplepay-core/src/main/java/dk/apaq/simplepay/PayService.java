package dk.apaq.simplepay;


import dk.apaq.simplepay.data.service.IEventService;
import dk.apaq.simplepay.data.service.IMerchantService;
import dk.apaq.simplepay.data.service.ITokenService;
import dk.apaq.simplepay.data.service.ITransactionService;
import dk.apaq.simplepay.data.service.IUserService;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The default implementation of the IPayService interface.<br><br>
 * This implemenation expects a Spring <code>ApplicationConext</code> to be injected via the <code>setApplicationContext</code> method 
 * before being used. 
 */
public class PayService implements IPayService {

    private static final Logger LOG = LoggerFactory.getLogger(PayService.class);
    
    private IMerchantService merchantService;
    private IUserService userService;
    private ITokenService tokenService;
    private ITransactionService transactionService;
    private IEventService eventService;
    private PaymentGatewayManager paymentGatewayManager;

    public void setPaymentGatewayManager(PaymentGatewayManager paymentGatewayManager) {
        this.paymentGatewayManager = paymentGatewayManager;
    }

    public void setEventService(IEventService eventService) {
        this.eventService = eventService;
    }

    public void setMerchantService(IMerchantService merchantService) {
        this.merchantService = merchantService;
    }

    public void setTokenService(ITokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void setTransactionService(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public IUserService getUserService() {
        return userService;
    }

    public ITokenService getTokenService() {
        return tokenService;
    }

    public ITransactionService getTransactionService() {
        return transactionService;
    }

    public IEventService getEventService() {
        return eventService;
    }

    public IMerchantService getMerchantService() {
        return merchantService;
    }
    

    
}

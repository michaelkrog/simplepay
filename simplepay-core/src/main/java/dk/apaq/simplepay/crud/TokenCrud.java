package dk.apaq.simplepay.crud;

import dk.apaq.crud.jpa.EntityManagerCrudForSpring;
import dk.apaq.simplepay.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import dk.apaq.simplepay.common.PaymentMethod;
import dk.apaq.simplepay.gateway.DirectPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.util.RequestInformationHelper;
import javax.persistence.EntityManager;



/**
 *
 * @author michael
 */
public class TokenCrud extends EntityManagerCrudForSpring<String, Token> implements ITokenCrud {

    @Autowired
    private PaymentGatewayManager gatewayManager;
    
    @Autowired
    private IPayService service;
    
    public TokenCrud(EntityManager em) {
        super(em, Token.class);
        
    }

    @Transactional
    public Token createNew(PaymentGatewayType gatewayType, String orderNumber, String description) {
        Token token = createAndRead(new Token(gatewayType, orderNumber, description));
        
        TokenEvent evt = new TokenEvent(token, "Token created.", service.getCurrentUsername(), RequestInformationHelper.getRemoteAddress());
        service.getEvents(token.getMerchant(), TokenEvent.class).create(evt);
        
        return token;
    }

    @Transactional
    public Token authorizedRemote(Token token, String currency, long amount, PaymentMethod paymentMethod, int expireMonth, int expireYear, String cardNumberTruncated, String remoteTransactionID) {
        token = read(token.getId());
        token.setCurrency(currency);
        token.setAuthorized(true);
        token.setAuthorizedAmount(amount);
        token.setPaymentMethod(paymentMethod);
        token.setCardExpireMonth(expireMonth);
        token.setCardExpireYear(expireYear);
        token.setCardNumberTruncated(cardNumberTruncated);
        token.setGatewayTransactionId(remoteTransactionID);
        token = update(token);
        
        TokenEvent evt = new TokenEvent(token, "Authorized remotely..", service.getCurrentUsername(), RequestInformationHelper.getRemoteAddress());
        service.getEvents(token.getMerchant(), TokenEvent.class).create(evt);
        
        
        return token;
    }

    @Transactional
    public Token authorize(Token token, String currency, long amount, PaymentMethod method, String cardNumber, String cvd, int expireMonth, int expireYear) {
        token = read(token.getId());
        token.setCurrency(currency);
        token.setAuthorized(true);
        token.setAuthorizedAmount(amount);
        token.setPaymentMethod(method);
        token.setCardExpireMonth(expireMonth);
        token.setCardExpireYear(expireYear);
        token.setCardNumber(cardNumber);
        token.setCardCvd(cvd);
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(token.getGatewayType());
        if(!(gateway instanceof DirectPaymentGateway)) {
            throw new ClassCastException("The gatewaytype specified by the token is not a DirectPaymentGateway and therefore authorize must be done remotely.");
        }
        
        ((DirectPaymentGateway)gateway).authorize(token, amount, currency, currency, currency);
        
        token = update(token);
        
        TokenEvent evt = new TokenEvent(token, "Authorized directly.", service.getCurrentUsername(), RequestInformationHelper.getRemoteAddress());
        service.getEvents(token.getMerchant(), TokenEvent.class).create(evt);
        
        
        return token;
    }
    
    
    
}

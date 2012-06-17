package dk.apaq.simplepay.crud;

import dk.apaq.simplepay.common.PaymentMethod;
import dk.apaq.simplepay.gateway.DirectPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Token;
import java.io.InvalidClassException;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michael
 */
public class TokenCrud extends EntityManagerCrudForSpring<String, Token> implements ITokenCrud {

    @Autowired
    private PaymentGatewayManager gatewayManager;
    
    public TokenCrud(EntityManager em) {
        super(em, Token.class);
        
    }

    @Transactional
    public Token createNew(PaymentGatewayType gatewayType, String orderNumber, String description) {
        Token token = new Token();
        token.setOrderNumber(orderNumber);
        token.setGatewayType(gatewayType);
        token.setDescription(description);
        return createAndRead(token);
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
        return update(token);
    }

    @Transactional
    public Token authorize(Token token, String currency, long amount, PaymentMethod method, String cardNumber, int cvd, int expireMonth, int expireYear) {
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
        
        return update(token);
    }
    
    
    
    
}

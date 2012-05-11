package dk.apaq.simplepay.crud;

import dk.apaq.simplepay.common.PaymentMethod;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Token;
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
    public Token authorize(Token token, String currency, long amount, PaymentMethod method, String cardNumber, String cvc, int expireMonth, int expireYear) {
        token = read(token.getId());
        token.setCurrency(currency);
        token.setAuthorized(true);
        token.setAuthorizedAmount(amount);
        token.setPaymentMethod(method);
        token.setCardExpireMonth(expireMonth);
        token.setCardExpireYear(expireYear);
        token.setCardNumber(cardNumber);
        //token.setCardCvc(cvc);
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(token.getMerchant(), token.getGatewayType());
        //gateway.authorize
        return update(token);
    }
    
    
    
    
}

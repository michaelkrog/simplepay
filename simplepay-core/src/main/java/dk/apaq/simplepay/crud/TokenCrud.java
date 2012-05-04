package dk.apaq.simplepay.crud;

import dk.apaq.simplepay.common.PaymentMethod;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Token;
import javax.persistence.EntityManager;

/**
 *
 * @author michael
 */
public class TokenCrud extends EntityManagerCrudForSpring<String, Token> implements ITokenCrud {

    public TokenCrud(EntityManager em) {
        super(em, Token.class);
        
    }

    public Token createNew(PaymentGatewayType gatewayType, String description) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Token authorizedRemote(Token token, String currency, long amount, PaymentMethod paymentMethod, int expireMonth, int expireYear, String cardNumberTruncated, String remoteTransactionID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Token authorize(Token token, String currency, long amount, PaymentMethod method, String cardNumber, String cvc, int expireMonth, int expireYear) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    
}

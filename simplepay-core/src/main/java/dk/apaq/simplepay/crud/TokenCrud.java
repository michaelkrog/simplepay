package dk.apaq.simplepay.crud;

import dk.apaq.crud.jpa.EntityManagerCrudForSpring;
import dk.apaq.simplepay.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Card;
import dk.apaq.simplepay.model.Token;
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

    public Token createNew(String certificate, PaymentGatewayType issuer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Token createNew(Card card) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    
}

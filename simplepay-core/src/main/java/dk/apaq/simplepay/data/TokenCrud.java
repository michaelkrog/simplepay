package dk.apaq.simplepay.data;

import dk.apaq.framework.repository.jpa.EntityManagerRepositoryForSpring;
import dk.apaq.simplepay.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Card;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.util.RequestInformationHelper;
import javax.persistence.EntityManager;



/**
 *
 * @author michael
 */
public class TokenCrud extends EntityManagerRepositoryForSpring<Token, String> implements ITokenCrud {

    @Autowired
    private PaymentGatewayManager gatewayManager;
    
    @Autowired
    private IPayService service;
    
    public TokenCrud(EntityManager em) {
        super(em, Token.class);
    }

    public Token createNew(Card card) {
        Token token = save(new Token(card));
        
        TokenEvent evt = new TokenEvent(token, "Token created.", service.getCurrentUsername(), RequestInformationHelper.getRemoteAddress());
        service.getEvents(token.getMerchant(), TokenEvent.class).save(evt);
        
        return token;
    }

    public void markExpired(Token token) {
        token.setExpired(true);
        save(token);
    }

    
    
    
}

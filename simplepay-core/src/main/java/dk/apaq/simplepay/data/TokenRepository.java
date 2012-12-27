package dk.apaq.simplepay.data;

import javax.persistence.EntityManager;

import dk.apaq.framework.repository.jpa.EntityManagerRepositoryForSpring;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Card;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.util.RequestInformationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michael
 */
public class TokenRepository extends EntityManagerRepositoryForSpring<Token, String> implements ITokenRepository {

    @Autowired
    private PaymentGatewayManager gatewayManager;
    @Autowired
    private IPayService service;

    public TokenRepository(EntityManager em) {
        super(em, Token.class);
    }

    @Override
    @Transactional
    public Token createNew(Card card) {
        Token token = save(new Token(card));

        TokenEvent evt = new TokenEvent(token, "Token created.", service.getCurrentUsername(), RequestInformationHelper.getRemoteAddress());
        service.getEvents(token.getMerchant(), TokenEvent.class).save(evt);

        return token;
    }

    @Override
    public void markExpired(Token token) {
        token.setExpired(true);
        save(token);
    }
}

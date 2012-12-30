package dk.apaq.simplepay.data;

import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.EntityManager;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.repository.jpa.EntityManagerRepositoryForSpring;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.util.RequestInformationHelper;
import org.jasypt.encryption.StringEncryptor;
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
    @Autowired
    private StringEncryptor encryptor;
    private final EntityManager em;

    public TokenRepository(EntityManager em) {
        super(em, Token.class);
        this.em = em;
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

    @Override
    public Token findOne(String id) {
        Token t = super.findOne(id);
        
        //We need to detach before decrypting
        em.detach(t);
        return decryptToken(t);
    }

    @Override
    public Token save(Token entity) {
        return encryptToken(super.save(entity));
    }

    @Override
    public List<Token> findAll(Criteria criteria) {
        List<Token> tokens = super.findAll(criteria);
        for(Token token : tokens) {
            decryptToken(token);
        }
        return tokens;
    }
    
    private Token encryptToken(Token token) {
        encryptObjectField(token.getData(), "cardNumber");
        encryptObjectField(token.getData(), "cvd");
        return token;
    }

    private Token decryptToken(Token token) {
        decryptObjectField(token.getData(), "cardNumber");
        decryptObjectField(token.getData(), "cvd");
        return token;
    }

    private void decryptObjectField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            String value = (String) field.get(object);
            field.set(object, encryptor.decrypt(value));
        } catch (NoSuchFieldException ex) {
            throw new UnknownError("Field '" + fieldName + "' should have existed.");
        } catch (IllegalAccessException ex) {
            throw new UnknownError("Field '" + fieldName + "' should have been accessible.");
        }
    }

    private void encryptObjectField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            String value = (String) field.get(object);
            field.set(object, encryptor.encrypt(value));
        } catch (NoSuchFieldException ex) {
            throw new UnknownError("Field '" + fieldName + "' should have existed.");
        } catch (IllegalAccessException ex) {
            throw new UnknownError("Field '" + fieldName + "' should have been accessible.");
        }
    }
}

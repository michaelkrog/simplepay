package dk.apaq.simplepay.data;

import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.EntityManager;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.repository.jpa.EntityManagerRepository;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.util.IdGenerator;
import dk.apaq.simplepay.util.RequestInformationHelper;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michael
 */
@Transactional(readOnly = true)    
public class TokenRepository extends EntityManagerRepository<Token, String> implements ITokenRepository {

    @Autowired
    private PaymentGatewayManager gatewayManager;
    @Autowired
    private IPayService service;
    private final EntityManager em;
    private Merchant merchant;

    public TokenRepository(EntityManager em, Merchant merchant) {
        super(em, Token.class);
        this.em = em;
        this.merchant = merchant;
    }

    @Override
    @Transactional
    public Token createNew(Card card) {
        Token token = new Token(card);
        token.setId(IdGenerator.generateUniqueId("t"));
        token.setMerchant(merchant);
        token = super.save(token);

        TokenEvent evt = new TokenEvent(token, "Token created.", service.getCurrentUsername(), RequestInformationHelper.getRemoteAddress());
        service.getEvents(token.getMerchant(), TokenEvent.class).save(evt);
        return token;
    }

    @Override
    public List<Token> findAll() {
        return findAll(DataAccess.appendMerchantCriteria(null, merchant));
    }

    @Override
    public List<Token> findAll(Criteria criteria) {
        return super.findAll(DataAccess.appendMerchantCriteria(criteria, merchant));
    }
    
    @Override
    @Transactional
    public Token markExpired(String token) {
        Token t = findOne(token);
        t.setExpired(true);
        return super.save(t); 
    }

    @Override
    public <S extends Token> S save(S entity) {
        throw new UnsupportedOperationException("Use markExpired.");
    }

}

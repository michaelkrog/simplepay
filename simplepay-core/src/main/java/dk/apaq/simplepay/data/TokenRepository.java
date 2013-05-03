package dk.apaq.simplepay.data;

import java.util.Date;
import java.util.List;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.util.IdGenerator;
import dk.apaq.simplepay.util.RequestInformationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author michael
 */
@Transactional(readOnly = true)    
public class TokenRepository extends RepositoryWrapper<Token, String> implements ITokenRepository {

    private final IPayService service;
    private Merchant merchant;

    public TokenRepository(Repository<Token, String> repository, IPayService service) {
        super(repository);
        this.service = service;
        
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
    
    @Override
    @Transactional
    public Token createNew(Card card) {
        Date now = new Date();
        Token token = new Token(card);
        token.setMerchant(merchant);
        token.setDateCreated(now);
        token.setDateChanged(now);
        token = super.save(token);

        TokenEvent evt = new TokenEvent(token, "Token created.", service.getCurrentUsername(), RequestInformationHelper.getRemoteAddress());
        service.getEvents(token.getMerchant(), TokenEvent.class).save(evt);
        return token;
    }

    @Override
    public Iterable<Token> findAll() {
        return findAll(DataAccess.appendMerchantCriteria(null, merchant));
    }

    @Override
    public Iterable<Token> findAll(Criteria criteria) {
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

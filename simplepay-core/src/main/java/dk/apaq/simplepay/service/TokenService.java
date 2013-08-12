package dk.apaq.simplepay.service;

import java.util.Date;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.PaymentContext;
import dk.apaq.simplepay.repository.IEventRepository;
import dk.apaq.simplepay.repository.ITokenRepository;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.util.RequestInformationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Javadoc
 */
public class TokenService extends BaseService<Token, String> {

    @Autowired private ITokenRepository repository;
    @Autowired private IEventRepository eventRepository;
    @Autowired private PaymentContext service;
    
    public Token createNew(Card card) {
        Merchant merchant = service.getUserService().getCurrentUser().getMerchant();
        Date now = new Date();
        Token token = new Token(card);
        token.setMerchant(merchant);
        token.setDateCreated(now);
        token.setDateChanged(now);
        token = repository.save(token);

        TokenEvent evt = new TokenEvent(token, "Token created.", service.getUserService().getCurrentUsername(), RequestInformationHelper.getRemoteAddress());
        //service.getEvents(token.getMerchant(), TokenEvent.class).save(evt);
        return token;
    }

    public Token markExpired(String token) {
        Token t = repository.findOne(token);
        t.setExpired(true);
        return repository.save(t); 
    }

    @Override
    public Iterable<Token> findAll() {
        Merchant merchant = service.getUserService().getCurrentUser().getMerchant();
        return repository.findByMerchant(merchant);
    }
    
    @Override
    public Page<Token> findAll(Pageable pageable) {
        Merchant merchant = service.getUserService().getCurrentUser().getMerchant();
        return repository.findByMerchant(merchant, pageable);
    }

    @Override
    public Token findOne(String token) {
        Merchant merchant = service.getUserService().getCurrentUser().getMerchant();
        return repository.findByMerchantAndId(merchant, token);
    }
    
    
}

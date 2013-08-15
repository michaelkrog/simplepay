package dk.apaq.simplepay.service;

import java.util.Date;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.PaymentContext;
import dk.apaq.simplepay.repository.IEventRepository;
import dk.apaq.simplepay.repository.ITokenRepository;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.repository.ISystemUserRepository;
import dk.apaq.simplepay.util.RequestInformationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Javadoc
 */
public class TokenService extends BaseService<Token, ITokenRepository> {

    private IEventRepository eventRepository;
    private UserService userService;

    @Autowired 
    public void setEventRepository(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Autowired 
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    
    public Token createNew(Merchant merchant, Card card) {
        Date now = new Date();
        Token token = new Token(card);
        token.setMerchant(merchant);
        token.setDateCreated(now);
        token.setDateChanged(now);
        token = repository.save(token);

        TokenEvent evt = new TokenEvent(token, "Token created.", userService.getCurrentUsername(), RequestInformationHelper.getRemoteAddress());
        //service.getEvents(token.getMerchant(), TokenEvent.class).save(evt);
        return token;
    }

    public Token markExpired(String token) {
        Token t = repository.findOne(token);
        t.setExpired(true);
        return repository.save(t); 
    }

    public Iterable<Token> findAll(Merchant merchant) {
        return repository.findByMerchant(merchant);
    }
    
    public Page<Token> findAll(Merchant merchant, Pageable pageable) {
        return repository.findByMerchant(merchant, pageable);
    }

    public Token findOne(Merchant merchant, String token) {
        return repository.findByMerchantAndId(merchant, token);
    }
    
    
}

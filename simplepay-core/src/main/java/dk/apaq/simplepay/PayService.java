package dk.apaq.simplepay;


import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.data.DataAccess;
import dk.apaq.simplepay.data.EventRepository;
import dk.apaq.simplepay.data.IEventRepository;
import dk.apaq.simplepay.data.ITokenRepository;
import dk.apaq.simplepay.data.TokenRepository;
import dk.apaq.simplepay.data.ITransactionRepository;
import dk.apaq.simplepay.data.TransactionRepository;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.security.ERole;
import dk.apaq.simplepay.util.IdGenerator;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 * The default implementation of the IPayService interface.<br><br>
 * This implemenation expects a Spring <code>ApplicationConext</code> to be injected via the <code>setApplicationContext</code> method 
 * before being used. 
 */
public class PayService implements IPayService {

    private static final Logger LOG = LoggerFactory.getLogger(PayService.class);
    
    private Repository<Merchant, String> merchantRepository;
    private Repository<SystemUser, String> userRepository;
    private Repository<Token, String> tokenRepository;
    private Repository<Transaction, String> transactionRepository;
    private Repository<BaseEvent, String> eventRepository;
    private PaymentGatewayManager paymentGatewayManager;

    public void setPaymentGatewayManager(PaymentGatewayManager paymentGatewayManager) {
        this.paymentGatewayManager = paymentGatewayManager;
    }
    
    public void setMerchantRepository(Repository<Merchant, String> merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public void setEventRepository(Repository<BaseEvent, String> eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void setTokenRepository(Repository<Token, String> tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void setTransactionRepository(Repository<Transaction, String> transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void setUserRepository(Repository<SystemUser, String> userRepository) {
        this.userRepository = userRepository;
    }
    
    
    @Override
    public ITransactionRepository getTransactions(Merchant merchant) {
        Validate.notNull(merchant, "merchant is null.");
        LOG.debug("Retrieving TransactionRepository");

        if (merchant.getId() == null) {
            throw new IllegalArgumentException("Merchant must have been persisted before used for retrieving transactions.");
        }

        ITransactionRepository rep = (ITransactionRepository) new TransactionRepository(transactionRepository, this, paymentGatewayManager);
        rep.setMerchant(merchant);
        return rep;
    }

    @Override
    public ITokenRepository getTokens(Merchant merchant) {
        DataAccess.checkMerchant(merchant);
        LOG.debug("Retrieving TokenRepository");
        
        ITokenRepository rep = (ITokenRepository) new TokenRepository(tokenRepository, this);
        
        rep.setMerchant(merchant);
        return rep;
    }

    @Override
    public <T extends BaseEvent> Repository<T, String> getEvents(Merchant merchant, Class<T> type) {
        DataAccess.checkMerchant(merchant);
        LOG.debug("Retrieving EventRepository for class {}", type);
        IEventRepository rep = (IEventRepository<T>) new EventRepository(eventRepository);
        rep.setMerchant(merchant);
        return rep;
    }

    @Override
    public Repository<Merchant, String> getMerchants() {
        LOG.debug("Retrieving MerchantRepository");
        return merchantRepository;
    }

    @Transactional
    @Override
    public SystemUser getOrCreatePublicUser(Merchant merchant) {
        DataAccess.checkMerchant(merchant);
        Iterable<SystemUser> list = getUserlist("merchant", merchant);

        for (SystemUser user : list) {
            if (user.getRoles().contains(ERole.PublicApiAccessor)) {
                return user;
            }
        }

        return getUsers().save(new SystemUser(merchant, IdGenerator.generateUniqueId(), "", ERole.PublicApiAccessor));
    }

    @Transactional
    @Override
    public SystemUser getOrCreatePrivateUser(Merchant merchant) {
        DataAccess.checkMerchant(merchant);
        Iterable<SystemUser> list = getUserlist("merchant", merchant);

        for (SystemUser user : list) {
            if (user.getRoles().contains(ERole.PrivateApiAccessor)) {
                return user;
            }
        }

        return getUsers().save(new SystemUser(merchant, IdGenerator.generateUniqueId(), "", ERole.PrivateApiAccessor));
    }

    @Override
    public Repository<SystemUser, String> getUsers() {
        LOG.debug("Retrieving SystemUserRepository");
        return userRepository;
    }

    @Override
    public SystemUser getUser(String username) {
        return getUser("username", username);
    }

    @Override
    public String getCurrentUsername() {
        SystemUser user = getCurrentUser();
        return user == null ? "Anonymous" : user.getUsername();
    }

    @Override
    public SystemUser getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUser(username);
    }

    private SystemUser getUser(String key, String value) {
        Rule rule = Rules.equals(key, value);
        return getUsers().findOne(new Criteria(rule));
    }

    private Iterable<SystemUser> getUserlist(String key, Object value) {
        Rule rule = Rules.equals(key, value);
        return getUsers().findAll(new Criteria(rule));
    }

    @Override
    public Transaction getTransactionByRefId(Merchant m, String refId) {
        Rule rule = Rules.equals("refId", refId);
        return getTransactions(m).findOne(new Criteria(rule));
    }
}

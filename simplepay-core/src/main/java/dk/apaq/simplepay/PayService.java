package dk.apaq.simplepay;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.data.DataAccess;
import dk.apaq.simplepay.data.ITokenRepository;
import dk.apaq.simplepay.data.ITransactionRepository;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.security.ERole;
import dk.apaq.simplepay.util.IdGenerator;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 * The default implementation of the IPayService interface.<br><br>
 * This implemenation expects a Spring <code>ApplicationConext</code> to be injected via the <code>setApplicationContext</code> method 
 * before being used. 
 */
public class PayService implements ApplicationContextAware, IPayService {

    private static final Logger LOG = LoggerFactory.getLogger(PayService.class);
    @PersistenceContext
    private EntityManager em;
    private ApplicationContext context;
    private Repository<Merchant, String> merchantRep;
    private Repository<SystemUser, String> userRep;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.context = ac;
    }

    @Override
    public ITransactionRepository getTransactions(Merchant merchant) {
        Validate.notNull(merchant, "merchant is null.");
        LOG.debug("Retrieving TransactionRepository");

        if (merchant.getId() == null) {
            throw new IllegalArgumentException("Merchant must have been persisted before used for retrieving transactions.");
        }

        return (ITransactionRepository) context.getBean("transactionRepository", em, merchant);
    }

    @Override
    public ITokenRepository getTokens(Merchant merchant) {
        DataAccess.checkMerchant(merchant);
        LOG.debug("Retrieving TokenRepository");
        return (ITokenRepository) context.getBean("tokenRepository", em, merchant);
    }

    @Override
    public <T extends Event> Repository<T, String> getEvents(Merchant merchant, Class<T> type) {
        DataAccess.checkMerchant(merchant);
        LOG.debug("Retrieving EventRepository for class {}", type);
        return (Repository<T, String>) context.getBean("eventRepository", em, merchant, type);
    }

    @Override
    public Repository<Merchant, String> getMerchants() {
        LOG.debug("Retrieving MerchantRepository");
        if (merchantRep == null) {
            merchantRep = (Repository<Merchant, String>) context.getBean("merchantRepository", em, this);
        }
        return merchantRep;
    }

    @Transactional
    @Override
    public SystemUser getOrCreatePublicUser(Merchant merchant) {
        DataAccess.checkMerchant(merchant);
        List<SystemUser> list = getUserlist("merchant", merchant);

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
        List<SystemUser> list = getUserlist("merchant", merchant);

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
        if (userRep == null) {
            userRep = (Repository) context.getBean("repository", em, SystemUser.class);
        }
        return userRep;
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
        return getUsers().findFirst(new Criteria(rule));
    }

    private List<SystemUser> getUserlist(String key, Object value) {
        Rule rule = Rules.equals(key, value);
        return getUsers().findAll(new Criteria(rule));
    }

    @Override
    public Transaction getTransactionByRefId(Merchant m, String refId) {
        Rule rule = Rules.equals("refId", refId);
        return getTransactions(m).findFirst(new Criteria(rule));
    }
}

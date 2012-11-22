package dk.apaq.simplepay;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.framework.repository.Repository;
import dk.apaq.framework.repository.RepositoryNotifier;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.data.DataAccess;
import dk.apaq.simplepay.data.ITokenCrud;
import dk.apaq.simplepay.data.ITransactionCrud;
import dk.apaq.simplepay.security.ERole;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.util.IdGenerator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author krog
 */
public class PayService implements ApplicationContextAware, IPayService {
    
    private static final Logger LOG = LoggerFactory.getLogger(PayService.class);
    
    @PersistenceContext
    private EntityManager em;
    
    private ApplicationContext context;
    private Repository<Merchant, String> merchantRep;
    private Repository<SystemUser, String> userRep;
    private DataAccess.MerchantSecurity merchantSecurity = new DataAccess.MerchantSecurity(this);

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.context=ac;
    }
    
    @Override
    public ITransactionCrud getTransactions(Merchant merchant) {
        LOG.debug("Retrieving TransactionCrud");
        
        if(merchant.getId() == null) {
            throw new IllegalArgumentException("Merchant must have been persisted before used for retrieving transactions.");
        }
        
        ITransactionCrud crud =  (ITransactionCrud) context.getBean("transactionCrud", em);
        ((RepositoryNotifier)crud).addListener(new DataAccess.TransactionSecurity(this, merchant));
        
        return crud;
    }
    
    @Override
    public ITokenCrud getTokens(Merchant merchant) {
        LOG.debug("Retrieving TokenCrud");
        
        if(merchant.getId() == null) {
            throw new IllegalArgumentException("Merchant must have been persisted before used for retrieving tokens.");
        }
        
        ITokenCrud crud = (ITokenCrud) context.getBean("tokenCrud", em);
        ((RepositoryNotifier)crud).addListener(new DataAccess.TokenSecurity(this, merchant));
        
        return crud;
            
    }

    public <T extends Event> Repository<T, String> getEvents(Merchant merchant, Class<T> type) {
        LOG.debug("Retrieving TransactionCrud");
        
        if(merchant.getId() == null) {
            throw new IllegalArgumentException("Merchant must have been persisted before used for retrieving transactions.");
        }
        
        Repository<T, String> crud = (Repository<T, String>) context.getBean("crud", em, type);
        ((RepositoryNotifier)crud).addListener(new DataAccess.EventSecurity(this, merchant));
        
        return crud;
    }
    
    @Override
    public Repository<Merchant, String> getMerchants() {
        LOG.debug("Retrieving MerchantCrud");
        if(merchantRep==null) {
            merchantRep = (Repository<Merchant, String>)context.getBean("crud", em, Merchant.class);
            ((RepositoryNotifier)merchantRep).addListener(merchantSecurity);
        }
        return merchantRep;
    }
    
    
    @Transactional
    @Override
    public SystemUser getOrCreatePublicUser(Merchant merchant) {
        List<SystemUser> list = getUserlist("merchant", merchant);
        
        for(SystemUser user : list) {
            if(user.getRoles().contains(ERole.PublicApiAccessor)) {
                return user;
            }
        }
        
        return getUsers().save(new SystemUser(merchant, IdGenerator.generateUniqueId(), "", ERole.PublicApiAccessor));
    }
    
    @Transactional
    @Override
    public SystemUser getOrCreatePrivateUser(Merchant merchant) {
        List<SystemUser> list = getUserlist("merchant", merchant);
        
        for(SystemUser user : list) {
            if(user.getRoles().contains(ERole.PrivateApiAccessor)) {
                return user;
            }
        }
        
        return getUsers().save(new SystemUser(merchant, IdGenerator.generateUniqueId(), "", ERole.PrivateApiAccessor));
    }
    
    @Override
    public Repository<SystemUser, String> getUsers() {
        LOG.debug("Retrieving SystemUserCrud");
        if(userRep==null) {
            userRep = (Repository) context.getBean("crud", em, SystemUser.class);
        }
        return userRep;
    }
    
    @Override
    public SystemUser getUser(String username) {
        return getUser("username", username);
    }

    public String getCurrentUsername() {
        SystemUser user = getCurrentUser();
        return user == null ? "Anonymous" : user.getUsername();
    }
    
    
    @Override
    public SystemUser getCurrentUser() {
        if(SecurityContextHolder.getContext().getAuthentication() == null) {
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
    public Transaction getTransactionByRefId(Merchant m, String orderNumber) {
        Rule rule = Rules.equals("refId", orderNumber);
        return getTransactions(m).findFirst(new Criteria(rule));
    }
}

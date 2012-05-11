package dk.apaq.simplepay;

import dk.apaq.crud.Crud;
import dk.apaq.crud.Crud.Complete;
import dk.apaq.crud.CrudNotifier;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.crud.CrudSecurity;
import dk.apaq.simplepay.crud.ITokenCrud;
import dk.apaq.simplepay.crud.ITransactionCrud;
import dk.apaq.simplepay.model.Role;
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
    private Crud.Complete<String, Merchant> merchantCrud;
    private Crud.Complete<String, SystemUser> userCrud;
    private CrudSecurity.MerchantSecurity merchantSecurity = new CrudSecurity.MerchantSecurity(this);

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
        ((CrudNotifier)crud).addListener(new CrudSecurity.TransactionSecurity(this, merchant));
        
        return crud;
    }
    
    @Override
    public ITokenCrud getTokens(Merchant merchant) {
        LOG.debug("Retrieving TokenCrud");
        
        if(merchant.getId() == null) {
            throw new IllegalArgumentException("Merchant must have been persisted before used for retrieving tokens.");
        }
        
        ITokenCrud crud = (ITokenCrud) context.getBean("tokenCrud", em);
        ((CrudNotifier)crud).addListener(new CrudSecurity.TokenSecurity(this, merchant));
        
        return crud;
            
    }

    public <T extends Event> Complete<String, T> getEvents(Merchant merchant, Class<T> type) {
        LOG.debug("Retrieving TransactionCrud");
        
        if(merchant.getId() == null) {
            throw new IllegalArgumentException("Merchant must have been persisted before used for retrieving transactions.");
        }
        
        Complete<String, T> crud = (Crud.Complete<String,T>) context.getBean("crud", em, type);
        ((CrudNotifier)crud).addListener(new CrudSecurity.EventSecurity(this, merchant));
        
        return crud;
    }
    
    @Override
    public Crud.Complete<String, Merchant> getMerchants() {
        LOG.debug("Retrieving MerchantCrud");
        if(merchantCrud==null) {
            merchantCrud = (Crud.Complete) context.getBean("crud", em, Merchant.class);
            ((CrudNotifier)merchantCrud).addListener(merchantSecurity);
        }
        return merchantCrud;
    }
    
    
    @Transactional
    @Override
    public SystemUser getOrCreatePublicUser(Merchant merchant) {
        List<SystemUser> list = getUserlist("merchant", merchant);
        
        for(SystemUser user : list) {
            if(user.getRoles().contains(Role.PublicApiAccessor)) {
                return user;
            }
        }
        
        return getUsers().createAndRead(new SystemUser(merchant, IdGenerator.generateUniqueId(), "", Role.PublicApiAccessor));
    }
    
    @Transactional
    @Override
    public SystemUser getOrCreatePrivateUser(Merchant merchant) {
        List<SystemUser> list = getUserlist("merchant", merchant);
        
        for(SystemUser user : list) {
            if(user.getRoles().contains(Role.PrivateApiAccessor)) {
                return user;
            }
        }
        
        return getUsers().createAndRead(new SystemUser(merchant, IdGenerator.generateUniqueId(), "", Role.PrivateApiAccessor));
    }
    
    @Override
    public Crud.Complete<String, SystemUser> getUsers() {
        LOG.debug("Retrieving SystemUserCrud");
        if(userCrud==null) {
            userCrud = (Crud.Complete) context.getBean("crud", em, SystemUser.class);
        }
        return userCrud;
    }
    
    @Override
    public SystemUser getUser(String username) {
        return getUser("username", username);
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
        Filter filter = new CompareFilter(key, value, CompareFilter.CompareType.Equals);
        List<SystemUser> list = getUsers().list(filter, null);
        return list.isEmpty() ? null : list.get(0);
    }
    
    private List<SystemUser> getUserlist(String key, Object value) {
        Filter filter = new CompareFilter(key, value, CompareFilter.CompareType.Equals);
        return getUsers().list(filter, null);
    }
    
    @Override
    public Transaction getTransactionByOrderNumber(Merchant m, String orderNumber) {
        Filter filter = new CompareFilter("orderNumber", orderNumber, CompareFilter.CompareType.Equals);
        List<Transaction> list = getTransactions(m).list(filter, null);
        return list.isEmpty() ? null : list.get(0);
    }
}

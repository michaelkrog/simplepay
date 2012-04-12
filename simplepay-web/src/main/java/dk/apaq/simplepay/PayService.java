package dk.apaq.simplepay;

import dk.apaq.crud.Crud;
import dk.apaq.crud.Crud.Complete;
import dk.apaq.crud.CrudNotifier;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.security.CrudSecurity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author krog
 */
public class PayService implements ApplicationContextAware {
    
    private static final Logger LOG = LoggerFactory.getLogger(PayService.class);
    
    @PersistenceContext
    private EntityManager em;
    
    private ApplicationContext context;
    private Crud.Complete<String, Merchant> merchantCrud;
    private CrudSecurity.MerchantSecurity merchantSecurity = new CrudSecurity.MerchantSecurity();

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.context=ac;
    }
    
    public Crud.Complete<String, Transaction> getTransactions(Merchant merchant) {
        LOG.debug("Retrieving TransactionCrud");
        
        if(merchant.getId() == null) {
            throw new IllegalArgumentException("Merchant must have been persisted before used for retrieving transactions.");
        }
        
        Complete<String, Transaction> crud = (Crud.Complete<String, Transaction>) context.getBean("transactionCrud", em, merchant);
        ((CrudNotifier)crud).addListener(new CrudSecurity.TransactionSecurity(merchant));
        
        return crud;
            
    }
    
    public Crud.Complete<String, Merchant> getMerchants() {
        LOG.debug("Retrieving MerchantCrud");
        if(merchantCrud==null) {
            merchantCrud = (Crud.Complete) context.getBean("merchantCrud", em);
            ((CrudNotifier)merchantCrud).addListener(merchantSecurity);
        }
        return merchantCrud;
    }
    
    public Merchant getMerchantBySecretKey(String secretKey) {
        return null;
    }

    public Merchant getMerchantByPublicKey(String publicKey) {
        return null;
    }
    
    public Merchant getMerchantByUsername(String username) {
        return null;
    }

}

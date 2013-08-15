
package dk.apaq.simplepay.service;

import java.util.Date;

import javax.annotation.PostConstruct;
import dk.apaq.simplepay.PaymentContext;
import dk.apaq.simplepay.repository.IMerchantRepository;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.repository.ISystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

/**
 * Javadoc
 */
public class MerchantService  extends BaseService<Merchant, IMerchantRepository> {

    @Autowired
    private ISystemUserRepository systemUserRepository;

    
    @Override
    public Merchant save(Merchant merchant) {
        Date now = new Date();
        
        if (!merchant.isNew()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                throw new IllegalStateException("No authenticated user available.");
            }
            String username = auth.getName();
            SystemUser user = systemUserRepository.findByUsername(username);
            Merchant usersMerchant = user.getMerchant();
            merchant.setDateCreated(usersMerchant.getDateCreated());
            if (!usersMerchant.getId().equals(merchant.getId())) {
                throw new SecurityException("Not allowed to change other merchants.");
            }
        } else {
            merchant.setDateCreated(now);
        }
        
        merchant.setDateChanged(now);
        return repository.save(merchant);
    }
}

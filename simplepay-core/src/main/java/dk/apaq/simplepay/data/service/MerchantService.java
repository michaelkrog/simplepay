
package dk.apaq.simplepay.data.service;

import java.util.Date;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.data.IMerchantRepository;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Javadoc
 */
public class MerchantService implements IMerchantService {

    private IMerchantRepository repository;
    private IPayService service;

    @Override
    public Merchant save(Merchant merchant) {
        Date now = new Date();
        
        if (!merchant.isNew()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                throw new IllegalStateException("No authenticated user available.");
            }
            String username = auth.getName();
            SystemUser user = service.getUserService().getUser(username);
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

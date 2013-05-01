package dk.apaq.simplepay.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EntityManager;

import dk.apaq.framework.repository.Repository;
import dk.apaq.framework.repository.jpa.EntityManagerRepository;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Javadoc
 */
public class MerchantRepository extends RepositoryWrapper<Merchant, String> {

    private final IPayService service;
    
    public MerchantRepository(Repository<Merchant, String> repository, IPayService service) {
        super(repository);
        this.service = service;
    }

    @Override
    @Transactional
    public <S extends Merchant> S save(S entity) {
        Date now = new Date();
        
        if (entity.getId() != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                throw new IllegalStateException("No authenticated user available.");
            }
            String username = auth.getName();
            SystemUser user = service.getUser(username);
            Merchant usersMerchant = user.getMerchant();
            entity.setDateCreated(usersMerchant.getDateCreated());
            if (!usersMerchant.getId().equals(entity.getId())) {
                throw new SecurityException("Not allowed to change other merchants.");
            }
        } else {
            entity.setDateCreated(now);
        }
        
        entity.setDateChanged(now);
        return super.save(entity);
    }
}

package dk.apaq.simplepay.data;

import java.io.Serializable;

import javax.persistence.EntityManager;
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
public class MerchantRepository extends EntityManagerRepository<Merchant, String> {

    private final IPayService service;
    
    public MerchantRepository(EntityManager entityManager, IPayService service) {
        super(entityManager, Merchant.class);
        this.service = service;
    }

    @Override
    @Transactional
    public <S extends Merchant> S save(S entity) {
        if (entity.getId() != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                throw new IllegalStateException("No authenticated user available.");
            }
            String username = auth.getName();
            SystemUser user = service.getUser(username);
            Merchant usersMerchant = user.getMerchant();
            if (!usersMerchant.getId().equals(entity.getId())) {
                throw new SecurityException("Not allowed to change other merchants.");
            }
        }
        return super.save(entity);
    }
}

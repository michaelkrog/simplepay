package dk.apaq.simplepay.data;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.repository.jpa.EntityManagerRepository;
import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;

/**
 * Javadoc
 */
public class EventRepository extends EntityManagerRepository<BaseEvent, String> {

    private Merchant merchant;

    public EventRepository(EntityManager em, Merchant merchant, Class type) {
        super(em, type);
        this.merchant = merchant;
    }

    @Override
    public List<BaseEvent> findAll() {
        return findAll(DataAccess.appendMerchantCriteria(null, merchant));
    }

    @Override
    public List<BaseEvent> findAll(Criteria criteria) {
        return super.findAll(DataAccess.appendMerchantCriteria(criteria, merchant));
    }

    @Override
    public <S extends BaseEvent> S save(S entity) {
        if(entity.getId() != null && exists(entity.getId())) {
            throw new UnsupportedOperationException("An event cannot be saved again.");
        }
        
        Date now = new Date();
        entity.setMerchant(merchant);
        entity.setDateChanged(now);
        entity.setDateCreated(now);
        
        return super.save(entity);
    }
}

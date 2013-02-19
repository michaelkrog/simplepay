package dk.apaq.simplepay.data;

import java.util.List;

import javax.persistence.EntityManager;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.repository.jpa.EntityManagerRepository;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;

/**
 * Javadoc
 */
public class EventRepository extends EntityManagerRepository<Event, String> {

    private Merchant merchant;

    public EventRepository(EntityManager em, Merchant merchant, Class type) {
        super(em, type);
        this.merchant = merchant;
    }

    @Override
    public List<Event> findAll() {
        return findAll(DataAccess.appendMerchantCriteria(null, merchant));
    }

    @Override
    public List<Event> findAll(Criteria criteria) {
        return super.findAll(DataAccess.appendMerchantCriteria(criteria, merchant));
    }

    @Override
    public <S extends Event> S save(S entity) {
        if (entity.getMerchant() == null) {
            entity.setMerchant(merchant);
        }
        return super.save(entity);
    }
}

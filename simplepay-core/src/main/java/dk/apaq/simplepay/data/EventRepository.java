package dk.apaq.simplepay.data;

import java.util.Date;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;

/**
 * Javadoc
 */
public class EventRepository extends RepositoryWrapper<BaseEvent, String> implements IEventRepository<BaseEvent> {

    private Merchant merchant;

    public EventRepository(Repository<BaseEvent, String> repository) {
        super(repository);
    }

    @Override
    public Merchant getMerchant() {
        return merchant;
    }

    @Override
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
    
    @Override
    public Iterable<BaseEvent> findAll() {
        return findAll(DataAccess.appendMerchantCriteria(null, merchant));
    }

    @Override
    public Iterable<BaseEvent> findAll(Criteria criteria) {
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

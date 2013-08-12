
package dk.apaq.simplepay.service;

import java.util.Date;

import dk.apaq.simplepay.PaymentContext;
import dk.apaq.simplepay.repository.IEventRepository;
import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;

/**
 * Javadoc
 */
public class EventService  {

    private IEventRepository repository;
    private PaymentContext context;

    public void setRepository(IEventRepository repository) {
        this.repository = repository;
    }

    public void setContext(PaymentContext context) {
        this.context = context;
    }
    
    public Iterable<BaseEvent> findAll() {
        Merchant merchant = context.getUserService().getCurrentUser().getMerchant();
        return repository.findByMerchant(merchant);
    }

    public <S extends BaseEvent> S save(S event) {
        if(!event.isNew()) {
            throw new UnsupportedOperationException("An event cannot be saved again.");
        }
        
        Merchant merchant = context.getUserService().getCurrentUser().getMerchant();
        Date now = new Date();
        event.setMerchant(merchant);
        event.setDateChanged(now);
        event.setDateCreated(now);
        
        return repository.save(event);
    }

}

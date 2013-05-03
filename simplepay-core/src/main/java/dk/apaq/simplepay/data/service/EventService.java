
package dk.apaq.simplepay.data.service;

import java.util.Date;
import java.util.List;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.data.IEventRepository;
import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;

/**
 * Javadoc
 */
public class EventService implements IEventService {

    private IEventRepository repository;
    private IPayService service;
    
    public Iterable<BaseEvent> findAll() {
        Merchant merchant = service.getUserService().getCurrentUser().getMerchant();
        return repository.findByMerchant(merchant);
    }

    @Override
    public void save(BaseEvent event) {
        if(!event.isNew()) {
            throw new UnsupportedOperationException("An event cannot be saved again.");
        }
        
        Merchant merchant = service.getUserService().getCurrentUser().getMerchant();
        Date now = new Date();
        event.setMerchant(merchant);
        event.setDateChanged(now);
        event.setDateCreated(now);
        
        repository.save(event);
    }

}

package dk.apaq.simplepay.data;

import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;

/**
 * Javadoc
 */
public interface IEventRepository<T extends BaseEvent> extends Repository<T, String> {

    Merchant getMerchant();

    void setMerchant(Merchant merchant);
    
}

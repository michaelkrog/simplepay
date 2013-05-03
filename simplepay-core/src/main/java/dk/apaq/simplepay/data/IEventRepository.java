package dk.apaq.simplepay.data;

import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Javadoc
 */
public interface IEventRepository<T extends BaseEvent> extends PagingAndSortingRepository<T, String> {

    Iterable<BaseEvent> findByMerchant(Merchant merchant);
}

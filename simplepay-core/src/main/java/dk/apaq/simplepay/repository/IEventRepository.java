package dk.apaq.simplepay.repository;

import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Javadoc
 */
public interface IEventRepository extends PagingAndSortingRepository<BaseEvent, String> {

    Iterable<BaseEvent> findByMerchant(Merchant merchant);
}

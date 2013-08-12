package dk.apaq.simplepay.repository;

import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author michael
 */
public interface ITransactionRepository extends PagingAndSortingRepository<Transaction, String> {
    
     Transaction findByMerchantAndId(Merchant merchant, String id);
     Transaction findByMerchantAndRefId(Merchant merchant, String refId);
}

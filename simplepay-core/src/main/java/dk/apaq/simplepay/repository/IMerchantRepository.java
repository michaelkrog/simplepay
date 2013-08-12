package dk.apaq.simplepay.repository;

import java.io.Serializable;

import dk.apaq.simplepay.model.Merchant;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author krog
 */
public interface IMerchantRepository extends PagingAndSortingRepository<Merchant, String> {
    
}

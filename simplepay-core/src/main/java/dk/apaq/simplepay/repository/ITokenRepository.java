package dk.apaq.simplepay.repository;

import java.io.Serializable;
import java.util.List;

import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author krog
 */
public interface ITokenRepository extends PagingAndSortingRepository<Token, String> {
    
    List<Token> findByMerchant(Merchant merchant);
    Page<Token> findByMerchant(Merchant merchant, Pageable pageable);       
    
    Token findByMerchantAndId(Merchant merchant, String id);
}

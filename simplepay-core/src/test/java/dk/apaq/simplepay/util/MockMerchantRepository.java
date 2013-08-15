
package dk.apaq.simplepay.util;

import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.repository.IMerchantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Javadoc
 */
public class MockMerchantRepository extends MockRepositoryBase<Merchant> implements IMerchantRepository {

    @Override
    public Iterable<Merchant> findAll(Sort sort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<Merchant> findAll(Pageable pgbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

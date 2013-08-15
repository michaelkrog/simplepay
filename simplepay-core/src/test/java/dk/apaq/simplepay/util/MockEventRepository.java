
package dk.apaq.simplepay.util;

import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.repository.IEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Javadoc
 */
public class MockEventRepository extends MockRepositoryBase<BaseEvent> implements IEventRepository {

    @Override
    public Iterable<BaseEvent> findByMerchant(Merchant merchant) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<BaseEvent> findAll(Sort sort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<BaseEvent> findAll(Pageable pgbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

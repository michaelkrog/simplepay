
package dk.apaq.simplepay.util;

import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.repository.ITransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Javadoc
 */
public class MockTransactionRepository extends MockRepositoryBase<Transaction> implements ITransactionRepository {

    @Override
    public Transaction findByMerchantAndId(Merchant merchant, String id) {
        for(Transaction t : findAll()) {
            if(t.getMerchant().getId().equals(merchant.getId()) && id.equals(t.getId())) {
                return t;
            }
        }
        return null;
    }

    @Override
    public Transaction findByMerchantAndRefId(Merchant merchant, String refId) {
        for(Transaction t : findAll()) {
            if(t.getMerchant().getId().equals(merchant.getId()) && refId.equals(t.getRefId())) {
                return t;
            }
        }
        return null;
    }

    @Override
    public Iterable<Transaction> findAll(Sort sort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<Transaction> findAll(Pageable pgbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

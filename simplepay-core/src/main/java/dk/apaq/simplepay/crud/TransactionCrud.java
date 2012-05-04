package dk.apaq.simplepay.crud;

import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import javax.persistence.EntityManager;

/**
 *
 * @author michael
 */
public class TransactionCrud extends EntityManagerCrudForSpring<String, Transaction> implements ITransactionCrud {

    public TransactionCrud(EntityManager em) {
        super(em, Transaction.class);
        
    }

    public Transaction createNew(Token token, String orderNumber) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Transaction charge(Transaction transaction, long amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Transaction cancel(Transaction transaction) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Transaction refund(Transaction transaction, long amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}

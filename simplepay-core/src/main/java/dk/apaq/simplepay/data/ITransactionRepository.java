package dk.apaq.simplepay.data;

import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import org.joda.money.Money;

/**
 *
 * @author michael
 */
public interface ITransactionRepository extends Repository<Transaction, String> {
    
    Transaction createNew(Token token, String refId, Money money);
    
    Transaction charge(Transaction transaction, long amount);
    
    Transaction cancel(Transaction transaction);
    
    Transaction refund(Transaction transaction, long amount);
    
}

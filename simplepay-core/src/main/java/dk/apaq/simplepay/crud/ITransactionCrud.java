package dk.apaq.simplepay.crud;

import dk.apaq.crud.Crud;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import org.joda.money.Money;

/**
 *
 * @author michael
 */
public interface ITransactionCrud extends Crud.Filterable<String, Transaction> {
    
    Transaction createNew(Token token, String refId, Money money);
    
    Transaction charge(Transaction transaction, long amount);
    
    Transaction cancel(Transaction transaction);
    
    Transaction refund(Transaction transaction, long amount);
    
}

package dk.apaq.simplepay.crud;

import dk.apaq.crud.Crud;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;

/**
 *
 * @author michael
 */
public interface ITransactionCrud extends Crud.Filterable<String, Transaction> {
    
    Transaction createNew(Token token, String refId, String currency);
    
    Transaction charge(Transaction transaction, long amount);
    
    Transaction cancel(Transaction transaction);
    
    Transaction refund(Transaction transaction, long amount);
    
}

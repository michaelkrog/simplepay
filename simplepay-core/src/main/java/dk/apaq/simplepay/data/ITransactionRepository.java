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
    
    /**
     * Creates a New Transaction.
     * @param token The token to use for the new transaction.
     * @param refId The RefId to use for the transaction
     * @param money The amount and currency the transaction should be authorized to handle.
     * @return The new transaction.
     */
    Transaction createNew(Token token, String refId, Money money);
    
    /**
     * Charges an existing transaction.
     * @param transaction The transaction to charge.
     * @param amount The amount to charge which must be equal to or less than the initial amount.
     * @return The charged transaction.
     */
    Transaction charge(Transaction transaction, long amount);
    
    /**
     * Cancel a transaction before it has been charged.
     * @param transaction The transaction to cancel.
     * @return The cancelled transaction.
     */
    Transaction cancel(Transaction transaction);
    
    /**
     * Refunds a charged transaction.
     * @param transaction The transaction to refund.
     * @param amount The amount to refund which must equals to og less than the amount charged.
     * @return The refunded transaction.
     */
    Transaction refund(Transaction transaction, long amount);
    
}

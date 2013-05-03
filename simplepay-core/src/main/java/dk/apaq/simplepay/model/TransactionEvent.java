package dk.apaq.simplepay.model;

import javax.validation.constraints.NotNull;

import dk.apaq.simplepay.common.ETransactionStatus;

/**
 *
 * @author michael
 */
public class TransactionEvent extends BaseEvent {

    private String username;
    @NotNull
    private ETransactionStatus newStatus;
    @NotNull
    private String remoteAddress;
    private Transaction transaction;

    protected TransactionEvent() {
    }

    public TransactionEvent(Transaction transaction, String username, ETransactionStatus newStatus, String remoteAddress) {
        if (transaction == null) {
            throw new NullPointerException("transaction was null.");
        }
        this.transaction = transaction;
        this.username = username;
        this.newStatus = newStatus;
        this.remoteAddress = remoteAddress;
    }


    @Override
    public String getType() {
        return "transactionEvent";
    }

    public ETransactionStatus getNewStatus() {
        return newStatus;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getUser() {
        return username;
    }

    public Transaction getTransaction() {
        return transaction;
    }

}

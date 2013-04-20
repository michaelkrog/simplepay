package dk.apaq.simplepay.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import dk.apaq.simplepay.common.ETransactionStatus;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author michael
 */
@Entity
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

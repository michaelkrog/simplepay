package dk.apaq.simplepay.model;

import dk.apaq.simplepay.common.ETransactionStatus;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author michael
 */
@Entity
public class TransactionEvent implements Event {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    
    private String username;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date eventDate = new Date();
    
    @NotNull
    private ETransactionStatus newStatus;
    @NotNull
    private String remoteAddress;
    
    @ManyToOne
    @JsonIgnore
    private Merchant merchant;
    
    private String entityId;

    protected TransactionEvent() {
    }

    
    public TransactionEvent(Transaction transaction, String username, ETransactionStatus newStatus, String remoteAddress) {
        if(transaction == null) throw new NullPointerException("transaction was null.");
        this.entityId = transaction.getId();
        this.username = username;
        this.newStatus = newStatus;
        this.remoteAddress = remoteAddress;
    }

    
    public String getId() {
        return id;
    }

    public ETransactionStatus getNewStatus() {
        return newStatus;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public Date getTimestamp() {
        return eventDate;
    }

    public String getUser() {
        return username;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getEntityId() {
        return entityId;
    }

    
    
}

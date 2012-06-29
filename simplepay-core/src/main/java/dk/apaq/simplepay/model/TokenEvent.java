package dk.apaq.simplepay.model;

import dk.apaq.simplepay.common.TransactionStatus;
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
public class TokenEvent implements Event {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    
    private String username;
    private String message;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    private Date eventDate = new Date();
    
    
    @NotNull
    private String remoteAddress;
    
    @ManyToOne
    @JsonIgnore
    private Merchant merchant;
    
    private String entityId;

    protected TokenEvent() {
    }

    
    public TokenEvent(Token token, String message, String username, String remoteAddress) {
        if(token == null) throw new NullPointerException("token was null.");
        this.entityId = token.getId();
        this.message = message;
        this.username = username;
        this.remoteAddress = remoteAddress;
    }

    
    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
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

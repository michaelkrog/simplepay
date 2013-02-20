
package dk.apaq.simplepay.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 * Javadoc
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BaseEvent extends BaseEntityWithGeneratedId  {
    
    @ManyToOne
    @JsonIgnore
    private Merchant merchant;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @NotNull
    private Date eventDate = new Date();
    
    public abstract String getType();
    
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Date getTimestamp() {
        return eventDate;
    }

}

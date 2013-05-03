
package dk.apaq.simplepay.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Javadoc
 */
public abstract class BaseEvent extends BaseEntityWithGeneratedId  {
    
    @JsonIgnore
    private Merchant merchant;
    
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

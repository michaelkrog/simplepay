package dk.apaq.simplepay.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import dk.apaq.framework.common.beans.finance.Card;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author krog
 */
@Entity
public class Token extends BaseEntity {

    
    @NotNull
    private ETokenPurpose purpose = ETokenPurpose.SinglePayment;
    private boolean expired = false;
    private boolean test = false;
    
    @Embedded 
    private Card tokenData;
    
    //Variables we dont want in a JSON output is here.
    //TODO Move the ignore descision to the JSON mapper instead
    @NotNull
    @ManyToOne
    @JsonIgnore
    private Merchant merchant;

    protected Token() {
    }

    public Token(Card data) {
        this.tokenData = data;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public ETokenPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(ETokenPurpose purpose) {
        this.purpose = purpose;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public Card getData() {
        return tokenData;
    }

    public void setData(Card tokenData) {
        this.tokenData = tokenData;
    }
    
    
}

package dk.apaq.simplepay.model;

import dk.apaq.simplepay.common.CardType;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 *
 * @author krog
 */
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
public abstract class AbstractToken implements Token {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
        
    @NotNull
    private CardType cardType = CardType.Unknown;
    private String cardExpires;
    private String cardNumberTruncated;
    
    @NotNull
    @ManyToOne
    private Merchant merchant;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();
    
    @JsonIgnore
    private String gatewayTransactionId;
    
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentGatewayType gatewayType = PaymentGatewayType.Test;

    public AbstractToken() {
    }

    public AbstractToken(PaymentGatewayType gatewayType) {
        this.gatewayType = gatewayType;
    }
    
    public String getCardExpires() {
        return cardExpires;
    }

    public String getCardNumberTruncated() {
        return cardNumberTruncated;
    }

    public CardType getCardType() {
        return cardType;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public PaymentGatewayType getGatewayType() {
        return gatewayType;
    }

    public String getId() {
        return id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setCardExpires(String cardExpires) {
        this.cardExpires = cardExpires;
    }

    public void setCardNumberTruncated(String cardNumberTruncated) {
        this.cardNumberTruncated = cardNumberTruncated;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public void setGatewayType(PaymentGatewayType gatewayType) {
        this.gatewayType = gatewayType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
    
}

package dk.apaq.simplepay.model;

import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.common.CardType;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author krog
 */
@Entity
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    
    private long chargedAmount;
    private long refundedAmount;
    
    @NotNull
    private String orderNumber;

    @JsonIgnore
    @ManyToOne
    @NotNull
    private Merchant merchant;
    
    private String currency;
    private String description;
    
    private Token token;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionStatus status = TransactionStatus.New;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();

    public long getCapturedAmount() {
        return chargedAmount;
    }

    public void setCapturedAmount(long capturedAmount) {
        this.chargedAmount = capturedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }


    public long getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(long refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
    
}

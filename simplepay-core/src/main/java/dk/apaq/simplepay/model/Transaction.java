package dk.apaq.simplepay.model;

import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.common.CardType;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Transaction {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private long authorizedAmount;
    private long capturedAmount;
    private long refundedAmount;
    
    @NotNull
    private String orderNumber;

    @JsonIgnore
    @ManyToOne
    @NotNull
    private Merchant merchant;
    
    private String currency;
    private String description;
    
    @NotNull
    private CardType cardType = CardType.Unknown;
    private String cardExpires;
    private String cardNumberTruncated;
    
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
    private PaymentGatewayType gatewayType;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private TransactionStatus status = TransactionStatus.New;

    public long getAuthorizedAmount() {
        return authorizedAmount;
    }

    public void setAuthorizedAmount(long authorizedAmount) {
        this.authorizedAmount = authorizedAmount;
    }

    public long getCapturedAmount() {
        return capturedAmount;
    }

    public void setCapturedAmount(long capturedAmount) {
        this.capturedAmount = capturedAmount;
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

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public PaymentGatewayType getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(PaymentGatewayType gatewayType) {
        this.gatewayType = gatewayType;
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

    public String getCardExpires() {
        return cardExpires;
    }

    public void setCardExpires(String cardExpires) {
        this.cardExpires = cardExpires;
    }

    public String getCardNumberTruncated() {
        return cardNumberTruncated;
    }

    public void setCardNumberTruncated(String cardNumberTruncated) {
        this.cardNumberTruncated = cardNumberTruncated;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
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
    
    
    
}

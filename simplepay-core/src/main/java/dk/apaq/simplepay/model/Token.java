package dk.apaq.simplepay.model;

import dk.apaq.simplepay.common.PaymentMethod;
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
public class Token {
    
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
        
    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.Unknown;
    private int cardExpireMonth;
    private int cardExpireYear;
    private String cardCvd;
    private String cardNumberTruncated;
    private long authorizedAmount;
    private boolean authorized = false;
    private boolean used = false;
    
    private String orderNumber;
    private String description;
    
    @NotNull
    private String currency="USD";
    
    @NotNull
    @JsonIgnore
    private TokenPurpose purpose = TokenPurpose.SinglePayment;
    
    
    @NotNull
    @ManyToOne
    @JsonIgnore
    private Merchant merchant;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateChanged = new Date();
    
    //Variables we dont want in a JSON output is here.
    //TODO Move the ignore descision to the JSON mapper instead
    @JsonIgnore
    private String gatewayTransactionId;
    
    @JsonIgnore
    private String cardNumber;
    
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentGatewayType gatewayType = PaymentGatewayType.Test;

    
    public Token(String currency, PaymentGatewayType gatewayType) {
        this.currency = currency;
        this.gatewayType = gatewayType;
    }

    public Token() {
    }
    
    public String getCardNumber() {
        return cardNumber;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
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

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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
    

    

    public long getAuthorizedAmount() {
        return authorizedAmount;
    }

    public void setAuthorizedAmount(long authorizedAmount) {
        this.authorizedAmount = authorizedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TokenPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(TokenPurpose purpose) {
        this.purpose = purpose;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public boolean isUsed() {
        return used;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getCardNumberTruncated() {
        return cardNumberTruncated;
    }

    public void setCardNumberTruncated(String cardNumberTruncated) {
        this.cardNumberTruncated = cardNumberTruncated;
    }

    public int getCardExpireMonth() {
        return cardExpireMonth;
    }

    public void setCardExpireMonth(int cardExpireMonth) {
        this.cardExpireMonth = cardExpireMonth;
    }

    public int getCardExpireYear() {
        return cardExpireYear;
    }

    public void setCardExpireYear(int cardExpireYear) {
        this.cardExpireYear = cardExpireYear;
    }

    public String getCardCvd() {
        return cardCvd;
    }

    public void setCardCvd(String cardCvd) {
        this.cardCvd = cardCvd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    
    
    
    
}

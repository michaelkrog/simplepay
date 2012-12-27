package dk.apaq.simplepay.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.money.Money;

/**
 *
 * @author krog
 */
@Entity
public class Transaction extends BaseEntity {

    private long amount;
    private long amountCharged;
    private long amountRefunded;
    @NotNull
    private String refId;
    private boolean test;
    private String message;
    @JsonIgnore
    @ManyToOne
    @NotNull
    private Merchant merchant;
    @NotNull
    private String currency;
    private String description;
    @NotNull
    private String token;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ETransactionStatus status = ETransactionStatus.Authorized;
    @Enumerated(EnumType.STRING)
    @NotNull
    private EPaymentGateway gatewayType;

    protected Transaction() { /* EMPTY */ }

    public Transaction(String token, String refId, Money money, EPaymentGateway gatewayType) {
        this.token = token;
        this.refId = refId;
        this.amount = money.getAmountMinorLong();
        this.currency = money.getCurrencyUnit().getCurrencyCode();
        this.gatewayType = gatewayType;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public EPaymentGateway getGatewayType() {
        return gatewayType;
    }

    public ETransactionStatus getStatus() {
        return status;
    }

    public void setStatus(ETransactionStatus status) {
        this.status = status;
    }

    public long getAmount() {
        return amount;
    }

    public long getAmountCharged() {
        return amountCharged;
    }

    public void setAmountCharged(long amountCharged) {
        this.amountCharged = amountCharged;
    }

    public long getAmountRefunded() {
        return amountRefunded;
    }

    public void setAmountRefunded(long amountRefunded) {
        this.amountRefunded = amountRefunded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRefId() {
        return refId;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

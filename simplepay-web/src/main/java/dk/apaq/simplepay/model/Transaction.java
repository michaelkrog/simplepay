package dk.apaq.simplepay.model;

import dk.apaq.simplepay.gateway.PaymentGatewayType;

/**
 *
 * @author krog
 */
public class Transaction {
    private String id;
    private long authorizedAmount;
    private long capturedAmount;
    private long refundedAmount;
    private String orderNumber;
    private Merchant merchant;
    private String currency;
    private String description;
    private String gatewayTransactionId;
    private PaymentGatewayType gatewayType;
    private boolean captured;
    private boolean refunded;
    private boolean cancelled;

    public long getAuthorizedAmount() {
        return authorizedAmount;
    }

    public void setAuthorizedAmount(long authorizedAmount) {
        this.authorizedAmount = authorizedAmount;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
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

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public long getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(long refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    
}

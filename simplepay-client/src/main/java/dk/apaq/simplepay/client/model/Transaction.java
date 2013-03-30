package dk.apaq.simplepay.client.model;

/**
 *
 * @author krog
 */
public class Transaction  {

    private long amount;
    private long amountCharged;
    private long amountRefunded;
    private String refId;
    private boolean test;
    private String message;
    private String currency;
    private String description;
    private String token;
    private String status;
    private String gatewayTransactionId;
    
    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
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

    public void setStatus(String status) {
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

package dk.apaq.simplepay.gateway;

/**
 *
 * @author krog
 */
public interface PaymentGateway {
    
    public void setMerchantId(String merchantId);
    public void setMerchantSecret(String merchantSecret);
    
    /**
     * Captures an already authorized amount.
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     * @param transactionId The already authorized transaction.
     */
    public void capture(long amountInCents, String transactionId);
    
    /**
     * Authorizes a new amount for a transaction already authorized for recurrings transactions.
     * @param orderNumber The ordernumber.
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     * @param currency The currency code.
     * @param autocapture Wether to autocapture the amount.
     * @param transactionId The transactionid.
     */
    public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId);

    /**
     * Renews an existing auhorization.
     * @param amountInCents
     * @param transactionId 
     */
    public void renew(long amountInCents, String transactionId);

    /**
     * Refund a transaction
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     * @param transactionId 
     */
    public void refund(long amountInCents, String transactionId);
    
    /**
     * Cancels an transaction.
     * @param transactionId 
     */
    public void cancel(String transactionId);

    /**
     * Retrieves all information about the transaction.
     * @param transactionId
     * @return 
     */
    public PaymentInformation getPaymentInformation(String transactionId);

    
}

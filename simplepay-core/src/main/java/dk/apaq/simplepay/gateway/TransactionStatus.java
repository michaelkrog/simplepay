package dk.apaq.simplepay.gateway;

/**
 *
 * @author krog
 */
public enum TransactionStatus {
    /**
     * Transaction started, but not completed. Eg. the customer may have left the payment window without completing the payment.
     */
    New, 
    
    /**
     * Transaction authorized by acquirer and can be captured. The customer has completed the payment in the payment window.
     */
    Authorized, 
    
    /**
     * You have captured the money. You have asked the acquirer to transfer the money from the customers bank account to yours.
     */
    Captured, 
    
    /**
     * Transaction cancelled. You have cancelled the transaction, eg. if you or the customer does not want to complete the order.
     */
    Cancelled, 
    
    /**
     * Transaction refunded. You have returned money to the customers bank account, eg. you or the customer has cancelled a part of or the entire order.
     */
    Refunded, 
    
    /**
     * Transaction is a subscription. You can make several recurring payments from the same customer, eg. a subscription for a magazine or music download.
     */
    Subscribed

}

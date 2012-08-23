package dk.apaq.simplepay.common;

/**
 *
 * @author krog
 */
public enum ETransactionStatus {
    /**
     * Transaction authorized but not completed.
     */
    Authorized, 
    
    /**
     * You have charged the money. You have asked the acquirer to transfer the money from the customers bank account to yours.
     */
    Charged, 
    
    /**
     * Transaction cancelled. You have cancelled the transaction, eg. if you or the customer does not want to complete the order.
     */
    Cancelled, 
    
    /**
     * Transaction refunded. You have returned money to the customers bank account, eg. you or the customer has cancelled a part of or the entire order.
     */
    Refunded, 
    


}

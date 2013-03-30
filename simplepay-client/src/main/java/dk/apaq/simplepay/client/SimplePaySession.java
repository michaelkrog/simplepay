package dk.apaq.simplepay.client;

import dk.apaq.simplepay.client.model.Token;
import dk.apaq.simplepay.client.model.Transaction;
import org.joda.money.Money;

/**
 * Manages a session for communicating with SimplePay.
 * <br><br>
 * An application can have as many sessions it likes with each huding their own key.
 * In most system a single session will suffice, but larger multiuser systems might need
 * access to multiple SimplePay accounts through the same Java application.<br><br>
 * 
 * Example:<br>
 * <code>
 * SimplePaySession spay = new SimplePaySession(""<key>);
 * Token token = spay.createToken("<card>", 2015, 11, "<cvd>");
 * 
 */
public class SimplePaySession 
{
    private final String key;

    public SimplePaySession(String key) {
        this.key = key;
    }
    
    public Token createToken(String cardNo, int expireYear, int expireMonth, String cvd) {
        return null;
    }
    
    public Transaction createTransaction(String tokenId, String refId, Money amount) {
        return null;
    }
    
    public Transaction getTransaction(String id) {
        return null;
    }
    
    public Transaction chargeTransaction(String id) {
        return chargeTransaction(id, null);
    }
    
    public Transaction chargeTransaction(String id, Money amount) {
        return null;
    }
    
    public Transaction refundTransaction(String id) {
        return refundTransaction(id, null);
    }
    
    public Transaction refundTransaction(String id, Money amount) {
        return null;
    }
    
    public Transaction cancelTransaction(String id) {
        return null; 
    }
}

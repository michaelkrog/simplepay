/*
 * No one but emploeyees at Apaq is allowed to use this code.
 * It may not be copied or used in any context unless by Apaq.
 */
package dk.apaq.simplepay.gateway;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.Token;
import org.joda.money.Money;

/**
 *
 * @author krog
 */
public interface IPaymentGateway {

    /**
     * Authorizes a transaction
     */
    public void authorize(Token token, Money money, String orderId, String terminalId);
    
    /**
     * Cancels a transaction.
     */
    void cancel(Token token);

    /**
     * Captures an already authorized amount.
     *
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    void capture(Token token, long amountInCents);

    /**
     * Refund a transaction
     *
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    void refund(Token token, long amountInCents);

    /**
     * Renews an existing auhorization.
     *
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    void renew(Token token, long amountInCents);

    void setService(IPayService service);
}

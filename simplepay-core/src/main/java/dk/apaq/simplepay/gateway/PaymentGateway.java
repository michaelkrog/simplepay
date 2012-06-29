/*
 * No one but emploeyees at Apaq is allowed to use this code.
 * It may not be copied or used in any context unless by Apaq.
 */
package dk.apaq.simplepay.gateway;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author krog
 */
public interface PaymentGateway {

    /**
     * Cancels an transaction.
     */
    void cancel(Token token);

    /**
     * Captures an already authorized amount.
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    void capture(Token token, long amountInCents);

    /**
     * Refund a transaction
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    void refund(Token token, long amountInCents);

    /**
     * Renews an existing auhorization.
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    void renew(Token token, long amountInCents);

    void setService(IPayService service);
    
}

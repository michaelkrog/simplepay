/*
 * No one but emploeyees at Apaq is allowed to use this code.
 * It may not be copied or used in any context unless by Apaq.
 */
package dk.apaq.simplepay.gateway;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.ETokenPurpose;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import org.joda.money.Money;

/**
 *
 * @author krog
 */
public interface IPaymentGateway {

    /**
     * Authorizes a transaction.
     * @param merchant The merchant that wants to authorize.
     * @param access The access information to use for this merchant.
     * @param card The card to authorize the payment for.
     * @param money The amount and currency to authorize.
     * @param orderId The orderId this order can refer to.
     * @param terminalId The terminalId for the authorization.
     * @param purpose The purpose of the transaction (Single Payment og recurring.)
     */
    public void authorize(Merchant merchant, PaymentGatewayAccess access, Card card, Money money, String orderId, String terminalId, 
            ETokenPurpose purpose);
    
    /**
     * Cancels an transaction.
     */
    void cancel(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId);

    /**
     * Captures an already authorized amount.
     *
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    void capture(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents);

    /**
     * Refund a transaction
     *
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    void refund(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents);

    /**
     * Renews an existing auhorization.
     *
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    void renew(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents);

    void setService(IPayService service);
}

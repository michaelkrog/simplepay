package dk.apaq.simplepay.gateway;

import dk.apaq.simplepay.model.Token;

/**
 *
 * @author michael
 */
public interface HasPaymentInformation {
    /**
     * Retrieves all information about the transaction.
     */
    PaymentInformation getPaymentInformation(Token token);
}

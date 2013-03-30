/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.client.exceptions;

/**
 * This exceptions is the base exception for all payment related exceptions.<br><br>
 * In order to get more details about the specific Payment Exception, be sure to catch
 * the specialized exception.<br><br>
 * 
 * Example:<br>
 * <code><pre>
 * SimplePaySession spay = ...;
 * try {
 *    Token token = spay.createToken(...);
 *    spay.createTransaction(...);
 * } catch(InvalidCardException ex) {
 *    // InvalidCardException is derived from PaymentException but has details about the specific error.
 * } catch(PaymentException ex) {
 *    // PaymentException is the general payment exception without any details.
 * }
 * </pre></code>
 */
public class PaymentException extends RuntimeException {

    /**
     * Creates a new Payment Exception.
     * @param message The error message.
     * @param cause The cause of this error.
     */
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new Payment Exception.
     * @param message The error message.
     */
    public PaymentException(String message) {
        super(message);
    }

    
    
}

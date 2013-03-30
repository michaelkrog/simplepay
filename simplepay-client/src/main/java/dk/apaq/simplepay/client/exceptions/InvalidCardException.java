/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.client.exceptions;

/**
 * A specialized <code>PaymentException</code> for exception thrown when using invalid card information.
 */
public class InvalidCardException extends PaymentException {

    /**
     * Creates new instance.
     * @param message The error message.
     */
    public InvalidCardException(String message) {
        super(message);
    }

    /**
     * Creates new instance.
     * @param message The error message.
     * @param cause The cause.
     */
    public InvalidCardException(String message, Throwable cause) {
        super(message, cause);
    }

    
}

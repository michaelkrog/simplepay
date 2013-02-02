package dk.apaq.simplepay.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception that tells spring mvc that the request did not succeed.
 */
@ResponseStatus(value = HttpStatus.PAYMENT_REQUIRED)
public class RequestFailedException extends RuntimeException {

    public RequestFailedException(String message) {
        super(message);
    }

    public RequestFailedException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

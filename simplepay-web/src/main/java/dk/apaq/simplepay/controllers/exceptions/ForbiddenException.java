package dk.apaq.simplepay.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception that tells spring mvc that the request is unauthorized 
 * and should report so via standard Http codes(401)
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

package dk.apaq.simplepay.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author krog
 */
@ResponseStatus(value = HttpStatus.PAYMENT_REQUIRED)
public class RequestFailed extends RuntimeException {

    public RequestFailed(String message) {
        super(message);
    }

}

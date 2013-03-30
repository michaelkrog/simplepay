/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.controllers.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.apaq.simplepay.controllers.exceptions.ParameterException;
import dk.apaq.simplepay.controllers.exceptions.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;

/**
 * Javadoc
 */
public final class RestErrorUtil {

    public static RestError resolveRestError(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        RestError error;
        if(AccessDeniedException.class.isAssignableFrom(ex.getClass())) {
            error = new RestError("invalid_request_error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else if(MissingServletRequestParameterException.class.isAssignableFrom(ex.getClass())) {
            MissingServletRequestParameterException ext = (MissingServletRequestParameterException) ex;
            error = new RestError("invalid_request_error", ex.getMessage());
            error.setParameter(ext.getParameterName());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if(ResourceNotFoundException.class.isAssignableFrom(ex.getClass())) {
            error = new RestError("invalid_request_error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else if(ParameterException.class.isAssignableFrom(ex.getClass())) {
            error = handleParameterException((ParameterException)ex, request, response);
        } else if(IllegalArgumentException.class.isAssignableFrom(ex.getClass())) {
            error = new RestError("invalid_request_error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            error = new RestError("api_error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        
        return error;
    }
    
    private static RestError handleParameterException(ParameterException ex, HttpServletRequest request, HttpServletResponse response) {
            RestError error = new RestError("card_error", ex.getMessage());
            error.setParameter(ex.getParameterName());
            
            if("cardNumber".equals(ex.getParameterName())) {
                error.setCode("invalid_number");
            } else if("expireYear".equals(ex.getParameterName())) {
                error.setCode("invalid_expiry_year");
            } else if("expireMonth".equals(ex.getParameterName())) {
                error.setCode("invalid_expiry_month");
            } else if("cvd".equals(ex.getParameterName())) {
                error.setCode("invalid_cvd");
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return error;
    }
}

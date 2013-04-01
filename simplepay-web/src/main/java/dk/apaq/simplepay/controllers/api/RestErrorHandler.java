/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.controllers.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.apaq.simplepay.controllers.exceptions.ParameterException;
import dk.apaq.simplepay.controllers.exceptions.ResourceNotFoundException;
import dk.apaq.simplepay.gateway.PaymentException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.MissingServletRequestParameterException;

/**
 * Javadoc
 */
public class RestErrorHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ade) throws IOException, ServletException {
        handleThrowable(req, res, ade);
    }
    
    public void handleThrowable(HttpServletRequest req, HttpServletResponse res, Throwable ex) throws IOException {
        RestError error = buildRestError(req, res, ex);
        printRestError(req, res, error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ae) throws IOException, ServletException {
        handleThrowable(req, res, ae);
    }
    
    private void printRestError(HttpServletRequest req, HttpServletResponse res, RestError error) throws IOException {
        res.setContentType("application/json");
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("\t\"type\":\"").append(error.getType()).append("\",\n");
        sb.append("\t\"message\":\"").append(error.getMessage().replaceAll("\"", "\\\"")).append("\",\n");
        
        if("card_error".equals(error.getType())) {
            sb.append("\t\"code\":\"").append(error.getCode()).append("\",\n");
            sb.append("\t\"param\":\"").append(error.getParameter()).append("\",\n");
        }
        sb.append("}");
        res.getWriter().print(sb.toString());
    }
    
    private RestError buildRestError(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        RestError error;
        if(AccessDeniedException.class.isAssignableFrom(ex.getClass())) {
            error = new RestError("invalid_request_error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if(AuthenticationException.class.isAssignableFrom(ex.getClass())) {
            error = new RestError("invalid_request_error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if(MissingServletRequestParameterException.class.isAssignableFrom(ex.getClass())) {
            MissingServletRequestParameterException ext = (MissingServletRequestParameterException) ex;
            error = new RestError("invalid_request_error", ex.getMessage());
            error.setParameter(ext.getParameterName());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if(ResourceNotFoundException.class.isAssignableFrom(ex.getClass())) {
            error = new RestError("invalid_request_error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else if(ParameterException.class.isAssignableFrom(ex.getClass())) {
            error = buildFromParameterException((ParameterException)ex, request, response);
        } else if(IllegalArgumentException.class.isAssignableFrom(ex.getClass())) {
            error = new RestError("invalid_request_error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if(PaymentException.class.isAssignableFrom(ex.getClass())) {
            error = new RestError("card_error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
        } else {
            error = new RestError("api_error", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        
        return error;
    }
    
    private static RestError buildFromParameterException(ParameterException ex, HttpServletRequest request, HttpServletResponse response) {
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

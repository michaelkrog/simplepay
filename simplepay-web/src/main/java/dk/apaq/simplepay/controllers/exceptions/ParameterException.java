/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.controllers.exceptions;

/**
 * Javadoc
 */
public class ParameterException extends IllegalArgumentException {

    private final String parameterName;
    
    public ParameterException(String message, String parameterName) {
        super(message);
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }

}

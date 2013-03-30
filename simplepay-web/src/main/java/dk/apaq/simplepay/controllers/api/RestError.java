/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.controllers.api;

/**
 * Javadoc
 */
public class RestError {

    private final String type;
    private final String message;
    private String code;
    private String parameter;

    public RestError(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getParameter() {
        return parameter;
    }
    
}

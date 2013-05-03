package dk.apaq.simplepay.model;

import javax.validation.constraints.NotNull;

/**
 *
 * @author michael
 */
public class TokenEvent extends BaseEvent {

    private String username;
    @NotNull
    private String remoteAddress;
    private Token token;

    protected TokenEvent() {
    }

    public TokenEvent(Token token, String message, String username, String remoteAddress) {
        if (token == null) {
            throw new NullPointerException("token was null.");
        }
        this.token = token;
        this.username = username;
        this.remoteAddress = remoteAddress;
    }

    @Override
    public String getType() {
        return "tokenEvent";
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getUser() {
        return username;
    }

    public Token getToken() {
        return token;
    }

    
}

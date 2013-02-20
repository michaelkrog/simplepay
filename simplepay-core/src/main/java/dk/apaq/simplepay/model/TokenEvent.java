package dk.apaq.simplepay.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author michael
 */
@Entity
public class TokenEvent extends BaseEvent {

    private String username;
    private String message;
    @NotNull
    private String remoteAddress;
    private String entityId;

    protected TokenEvent() {
    }

    public TokenEvent(Token token, String message, String username, String remoteAddress) {
        if (token == null) {
            throw new NullPointerException("token was null.");
        }
        this.entityId = token.getId();
        this.message = message;
        this.username = username;
        this.remoteAddress = remoteAddress;
    }

    @Override
    public String getType() {
        return "tokenEvent";
    }

    public String getMessage() {
        return message;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getUser() {
        return username;
    }

    public String getEntityId() {
        return entityId;
    }
}

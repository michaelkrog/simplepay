package dk.apaq.simplepay.model;

import dk.apaq.simplepay.gateway.PaymentGatewayType;
import javax.persistence.Entity;

/**
 *
 * @author krog
 */
@Entity
public class RemoteAuthorizedToken extends AbstractToken {
    
    
    private long authorizedAmount;
    private String currency;

    public RemoteAuthorizedToken(PaymentGatewayType gatewayType) {
        super(gatewayType);
    }

    public RemoteAuthorizedToken() {
    }
    

    public long getAuthorizedAmount() {
        return authorizedAmount;
    }

    public void setAuthorizedAmount(long authorizedAmount) {
        this.authorizedAmount = authorizedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
}

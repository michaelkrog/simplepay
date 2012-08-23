package dk.apaq.simplepay.gateway;

import dk.apaq.simplepay.model.Token;
import org.joda.money.Money;

/**
 *
 * @author michael
 */
public interface IDirectPaymentGateway extends IPaymentGateway {
    
    public void authorize(Token token, Money money, String orderId, String terminalId);
    
}

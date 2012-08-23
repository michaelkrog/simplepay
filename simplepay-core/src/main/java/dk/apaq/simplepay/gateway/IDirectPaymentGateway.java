package dk.apaq.simplepay.gateway;

import dk.apaq.simplepay.model.Token;

/**
 *
 * @author michael
 */
public interface IDirectPaymentGateway extends IPaymentGateway {
    
    public void authorize(Token token, long amount, String currency, String orderId, String terminalId);
    
}
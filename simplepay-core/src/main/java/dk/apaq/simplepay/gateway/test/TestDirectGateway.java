package dk.apaq.simplepay.gateway.test;

import dk.apaq.simplepay.gateway.DirectPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author michael
 */
public class TestDirectGateway extends TestGateway implements DirectPaymentGateway {
    
    public void authorize(Token token, long amount, String currency, String orderId, String terminalId) {
        if(amount > 1000000) {
            throw new PaymentException("Amount to large");
        }
    }
}

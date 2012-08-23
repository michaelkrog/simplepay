package dk.apaq.simplepay.gateway.test;

import dk.apaq.simplepay.gateway.IDirectPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.model.Token;
import org.joda.money.Money;

/**
 *
 * @author michael
 */
public class TestDirectGateway extends TestGateway implements IDirectPaymentGateway {
    
    public void authorize(Token token, Money money, String orderId, String terminalId) {
        if(money.getAmountMinorLong() > 1000000) {
            throw new PaymentException("Amount to large");
        }
    }
}

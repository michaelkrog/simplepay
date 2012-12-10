package dk.apaq.simplepay.gateway.nets;

import dk.apaq.simplepay.gateway.AbstractPaymentGateway;
import dk.apaq.simplepay.gateway.IDirectPaymentGateway;
import dk.apaq.simplepay.model.Token;
import org.joda.money.Money;

/**
 *
 * @author michael
 */
public class Nets extends AbstractPaymentGateway implements IDirectPaymentGateway {

    public void authorize(Token token, Money money, String orderId, String terminalId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void cancel(Token token) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void capture(Token token, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void refund(Token token, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void renew(Token token, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

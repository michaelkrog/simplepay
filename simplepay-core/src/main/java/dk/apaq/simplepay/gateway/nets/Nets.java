package dk.apaq.simplepay.gateway.nets;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.gateway.AbstractPaymentGateway;
import dk.apaq.simplepay.gateway.DirectPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentInformation;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author michael
 */
public class Nets extends AbstractPaymentGateway implements DirectPaymentGateway {

    public void authorize(Token token, long amount, String currency, String orderId, String terminalId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void cancel(Token token) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void capture(Token token, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PaymentInformation getPaymentInformation(Token token) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void refund(Token token, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void renew(Token token, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}

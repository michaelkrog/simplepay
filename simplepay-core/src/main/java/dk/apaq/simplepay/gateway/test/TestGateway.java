package dk.apaq.simplepay.gateway.test;

import java.util.Random;
import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.model.ETokenPurpose;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import org.joda.money.Money;

/**
 *
 * @author krog
 */
public class TestGateway implements IPaymentGateway {

    private long ONE_MILLION = 100000000;
    private boolean fireRandomErrors = false;

    @Override
    public void authorize(Merchant marchant, PaymentGatewayAccess access, Card card, Money money, String orderId, String terminalId, ETokenPurpose purpose) {
        if (money.getAmountMinorLong() > ONE_MILLION) {
            throw new PaymentException("Amount to large. Maximum one million allowed.");
        }

        if (!card.isValid()) {
            throw new PaymentException("Card is not valid.");
        }
    }

    @Override
    public void cancel(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId) {
    }

    @Override
    public void capture(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        if (fireRandomErrors) {
            Random r = new Random();
            if (r.nextInt(10) == 5) {
                throw new PaymentException("Unknown error occured.");
            }
        }
    }

    @Override
    public void refund(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        if (fireRandomErrors) {
            Random r = new Random();
            if (r.nextInt(10) == 5) {
                throw new PaymentException("Unknown error occured.");
            }
        }
    }

    /*public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId) {
     throw new UnsupportedOperationException("Not supported yet.");
     }*/
    @Override
    public void renew(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setFireRandomErrors(boolean fireRandomErrors) {
        this.fireRandomErrors = fireRandomErrors;
    }
    
    
}

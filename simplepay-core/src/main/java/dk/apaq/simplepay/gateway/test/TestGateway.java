package dk.apaq.simplepay.gateway.test;

import dk.apaq.simplepay.gateway.AbstractPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.model.Token;
import org.joda.money.Money;

/**
 *
 * @author krog
 */
public class TestGateway extends AbstractPaymentGateway {

    public void authorize(Token token, Money money, String orderId, String terminalId) {
        if (money.getAmountMinorLong() > 1000000) {
            throw new PaymentException("Amount to large");
        }
    }
    
    public void cancel(Token token) {
        /*transaction.setStatus(TransactionStatus.Cancelled);
        service.getTransactions(merchant).update(transaction);*/
    }

    public void capture(Token token, long amountInCents) {
        /*transaction.setCapturedAmount(amountInCents);
        transaction.setStatus(TransactionStatus.Charged);
        service.getTransactions(merchant).update(transaction);*/
    }

    public void refund(Token token, long amountInCents) {
        /*transaction.setRefundedAmount(amountInCents);
        transaction.setStatus(TransactionStatus.Refunded);
        service.getTransactions(merchant).update(transaction);*/
    }

    /*public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId) {
    throw new UnsupportedOperationException("Not supported yet.");
    }*/
    
    public void renew(Token token, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}

package dk.apaq.simplepay.gateway.test;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.gateway.AbstractPaymentGateway;
import dk.apaq.simplepay.gateway.IDirectPaymentGateway;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentInformation;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author krog
 */
public abstract class TestGateway extends AbstractPaymentGateway {

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

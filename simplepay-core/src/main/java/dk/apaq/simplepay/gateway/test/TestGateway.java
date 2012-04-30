package dk.apaq.simplepay.gateway.test;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentInformation;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author krog
 */
public class TestGateway implements PaymentGateway {

    protected IPayService service;
    protected Merchant merchant;
    
    public TestGateway() {
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

    public PaymentInformation getPaymentInformation(Token token) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public void setService(IPayService service) {
        this.service = service;
    }
    
}

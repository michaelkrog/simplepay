package dk.apaq.simplepay.gateway.test;

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

    @Override
    public void authorize(Merchant marchant, PaymentGatewayAccess access, Card card, Money money, String orderId, String terminalId, ETokenPurpose purpose) {
        if (money.getAmountMinorLong() > 1000000) {
            throw new PaymentException("Amount to large");
        }
    }
    
    @Override
    public void cancel(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId) {
        /*transaction.setStatus(TransactionStatus.Cancelled);
        service.getTransactions(merchant).update(transaction);*/
    }

    @Override
    public void capture(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        /*transaction.setCapturedAmount(amountInCents);
        transaction.setStatus(TransactionStatus.Charged);
        service.getTransactions(merchant).update(transaction);*/
    }

    @Override
    public void refund(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        /*transaction.setRefundedAmount(amountInCents);
        transaction.setStatus(TransactionStatus.Refunded);
        service.getTransactions(merchant).update(transaction);*/
    }

    /*public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId) {
    throw new UnsupportedOperationException("Not supported yet.");
    }*/

    @Override
    public void renew(Merchant marchant, PaymentGatewayAccess access, String transactionId, String orderId, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}

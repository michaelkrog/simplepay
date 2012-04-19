package dk.apaq.simplepay.gateway.test;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentInformation;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionStatus;
import java.util.Locale;

/**
 *
 * @author krog
 */
public class TestGateway implements PaymentGateway {

    private IPayService service;
    private Merchant merchant;

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public void setService(IPayService service) {
        this.service = service;
    }
    
    public void capture(Transaction transaction, long amountInCents) {
        transaction.setCapturedAmount(amountInCents);
        transaction.setStatus(TransactionStatus.Captured);
        service.getTransactions(merchant).update(transaction);
    }

    /*public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }*/

    public void renew(Transaction transaction, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void refund(Transaction transaction, long amountInCents) {
        transaction.setRefundedAmount(amountInCents);
        transaction.setStatus(TransactionStatus.Refunded);
        service.getTransactions(merchant).update(transaction);
    }

    public void cancel(Transaction transaction) {
        transaction.setStatus(TransactionStatus.Cancelled);
        service.getTransactions(merchant).update(transaction);
    }

    public PaymentInformation getPaymentInformation(Transaction transaction) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public FormData generateFormdata(Transaction transaction, long amount, String currency, String returnUrl, String cancelUrl, String callbackUrl, Locale locale) {
        SystemUser publicUser = service.getOrCreatePublicUser(merchant);
        FormData data = new FormData();
        data.setUrl("/paymentwindow");
        data.getFields().put("publicKey", publicUser.getUsername());
        data.getFields().put("token", transaction.getId());
        data.getFields().put("amount", Long.toString(amount));
        data.getFields().put("currency", currency);
        data.getFields().put("returnUrl", returnUrl);
        data.getFields().put("cancelUrl", cancelUrl);
        
        return data;
    }

    
}

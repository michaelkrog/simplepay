package dk.apaq.simplepay.gateway.test;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentInformation;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.RemoteAuthorizedToken;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import java.util.Locale;
import org.springframework.security.authentication.rcp.RemoteAuthenticationProvider;

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
    
    public void capture(Token token, long amountInCents) {
        /*transaction.setCapturedAmount(amountInCents);
        transaction.setStatus(TransactionStatus.Charged);
        service.getTransactions(merchant).update(transaction);*/
    }

    /*public void recurring(String orderNumber, long amountInCents, String currency, boolean autocapture, String transactionId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }*/

    public void renew(Token token, long amountInCents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void refund(Token token, long amountInCents) {
        /*transaction.setRefundedAmount(amountInCents);
        transaction.setStatus(TransactionStatus.Refunded);
        service.getTransactions(merchant).update(transaction);*/
    }

    public void cancel(Token token) {
        /*transaction.setStatus(TransactionStatus.Cancelled);
        service.getTransactions(merchant).update(transaction);*/
    }


    public FormData generateFormdata(RemoteAuthorizedToken token, long amount, String currency, String returnUrl, String cancelUrl, String callbackUrl, Locale locale) {
        SystemUser publicUser = service.getOrCreatePublicUser(merchant);
        FormData data = new FormData();
        data.setUrl("/paymentwindow");
        data.getFields().put("publicKey", publicUser.getUsername());
        data.getFields().put("token", token.getId());
        data.getFields().put("amount", Long.toString(amount));
        data.getFields().put("currency", currency);
        data.getFields().put("returnUrl", returnUrl);
        data.getFields().put("cancelUrl", cancelUrl);
        
        return data;
    }


    public PaymentInformation getPaymentInformation(Token token) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}

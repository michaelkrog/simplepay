package dk.apaq.simplepay.gateway;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.RemoteAuthorizedToken;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author krog
 */
public interface PaymentGateway {
    
    public class FormData {
        private String url;
        private Map<String, String> fields = new LinkedHashMap<String, String>();

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, String> getFields() {
            return fields;
        }
        
    }
    
    public void setMerchant(Merchant merchant);
    
    public void setService(IPayService service);
    
    /**
     * Captures an already authorized amount.
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    public void capture(Token token, long amountInCents);
    
    /**
     * Authorizes a new amount for a transaction already authorized for recurrings transactions.
     * @param orderNumber The ordernumber.
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     * @param currency The currency code.
     * @param autocapture Wether to autocapture the amount.
     * @param transactionId The transactionid.
     */
    //public void recurring(Transaction transaction, String orderNumber, long amountInCents, String currency, boolean autocapture);

    /**
     * Renews an existing auhorization.
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    public void renew(Token token, long amountInCents);

    /**
     * Refund a transaction
     * @param amountInCents The amount in cents(smallest denominator in the currency.)
     */
    public void refund(Token token, long amountInCents);
    
    /**
     * Cancels an transaction.
     */
    public void cancel(Token token);

    /**
     * Retrieves all information about the transaction.
     */
    public PaymentInformation getPaymentInformation(Token token);

    /**
     * Generates form data to let the transaction be authorized remotely in a payment window.
     */
    public FormData generateFormdata(RemoteAuthorizedToken token, long amount, String currency, String returnUrl, String cancelUrl, String callbackUrl, Locale locale);
}

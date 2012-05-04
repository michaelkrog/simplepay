package dk.apaq.simplepay.gateway;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author krog
 */
public interface RemoteAuthPaymentGateway extends PaymentGateway {
    
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

    /**
     * Generates form data to let the transaction be authorized remotely in a payment window.
     */
    public FormData generateFormdata(Token token, long amount, String currency, String returnUrl, String cancelUrl, String callbackUrl, Locale locale);
}

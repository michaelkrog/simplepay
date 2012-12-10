package dk.apaq.simplepay.gateway;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import dk.apaq.simplepay.model.Token;

/**
 *
 * @author krog
 */
public interface IRemoteAuthPaymentGateway extends IPaymentGateway {

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

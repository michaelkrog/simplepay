package dk.apaq.simplepay.gateway.test;

import java.util.Locale;

import dk.apaq.simplepay.gateway.IRemoteAuthPaymentGateway;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;

/**
 *
 * @author krog
 */
public class TestRemoteGateway extends TestGateway implements IRemoteAuthPaymentGateway {

    public FormData generateFormdata(Token token, long amount, String currency, String returnUrl, String cancelUrl, String callbackUrl, Locale locale) {
        SystemUser publicUser = service.getOrCreatePublicUser(token.getMerchant());
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
}

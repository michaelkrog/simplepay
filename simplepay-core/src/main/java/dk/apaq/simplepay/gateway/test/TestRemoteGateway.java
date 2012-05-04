package dk.apaq.simplepay.gateway.test;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.gateway.RemoteAuthPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentInformation;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import java.util.Locale;
import org.springframework.security.authentication.rcp.RemoteAuthenticationProvider;

/**
 *
 * @author krog
 */
public class TestRemoteGateway extends TestGateway implements RemoteAuthPaymentGateway {

    


    public FormData generateFormdata(Token token, long amount, String currency, String returnUrl, String cancelUrl, String callbackUrl, Locale locale) {
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

    
}

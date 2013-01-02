package dk.apaq.simplepay;

import javax.annotation.PostConstruct;

import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.security.ERole;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author krog
 */
public class Bootstrap {

    @Autowired
    private IPayService payService;

    @PostConstruct
    public void doBootstrap() {
        if (payService.getMerchants().findAllIds().isEmpty()) {
            Merchant m = new Merchant();
            PaymentGatewayAccess aa = new PaymentGatewayAccess(EPaymentGateway.Test, "89898978", "29p61DveBZ79c3144LW61lVz1qrwk2gfAFCxPyi5sn49m3Y3IRK5M6SN5d8a68u7");
            m.getPaymentGatewayAccesses().add(aa);
            m = payService.getMerchants().save(m);

            SystemUser publicApiUser = payService.getUsers().save(new SystemUser(m, "qwerty", "", ERole.PublicApiAccessor));
            SystemUser privateApiUser = payService.getUsers().save(new SystemUser(m, "123456", "", ERole.PrivateApiAccessor));
            SystemUser merchantUser = payService.getUsers().save(new SystemUser(m, "michael.krog", "krogen", ERole.Merchant));

        }
    }
}

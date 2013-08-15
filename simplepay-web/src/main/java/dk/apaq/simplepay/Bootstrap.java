package dk.apaq.simplepay;

import javax.annotation.PostConstruct;

import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.gateway.IPaymentGateway;
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
    private PaymentContext context;

    @PostConstruct
    public void doBootstrap() {
        if (!context.getMerchantService().findAll().iterator().hasNext()) {
            Merchant m = new Merchant();
            PaymentGatewayAccess aa = new PaymentGatewayAccess(EPaymentGateway.Test, "89898978");
            m.getPaymentGatewayAccesses().add(aa);
            m = context.getMerchantService().save(m);

            SystemUser publicApiUser = context.getUserService().save(new SystemUser(m, "qwerty", "", ERole.PublicApiAccessor));
            SystemUser privateApiUser = context.getUserService().save(new SystemUser(m, "123456", "", ERole.PrivateApiAccessor));
            SystemUser merchantUser = context.getUserService().save(new SystemUser(m, "michael.krog", "krogen", ERole.Merchant));

        }
    }
}

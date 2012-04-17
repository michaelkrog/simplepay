package dk.apaq.simplepay;

import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Role;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;
import javax.annotation.PostConstruct;
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
        if(payService.getMerchants().listIds().isEmpty()) {
            Merchant m = new Merchant();
            m.setGatewayType(PaymentGatewayType.QuickPay);
            m.setGatewayUserId("89898978");
            m.setGatewaySecret("29p61DveBZ79c3144LW61lVz1qrwk2gfAFCxPyi5sn49m3Y3IRK5M6SN5d8a68u7");
            m = payService.getMerchants().createAndRead(m);
            
            SystemUser publicApiUser = payService.getUsers().createAndRead(new SystemUser(m, "qwerty", "", Role.PublicApiAccessor));
            SystemUser privateApiUser = payService.getUsers().createAndRead(new SystemUser(m, "123456", "", Role.PrivateApiAccessor));
            SystemUser merchantUser = payService.getUsers().createAndRead(new SystemUser(m, "michael.krog", "krogen", Role.Merchant));
            
        }
    }
}

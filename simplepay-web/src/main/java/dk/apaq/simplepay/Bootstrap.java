package dk.apaq.simplepay;

import dk.apaq.simplepay.model.Merchant;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author krog
 */
public class Bootstrap {
    
    @Autowired
    private PayService payService;
    
    @PostConstruct
    public void doBootstrap() {
        if(payService.getMerchants().listIds().isEmpty()) {
            Merchant m = new Merchant();
            m.setUsername("michael.krog");
            m.setPassword("krogen");
            m.setPublicKey("qwerty");
            m.setSecretKey("123456");
            payService.getMerchants().create(m);
        }
    }
}

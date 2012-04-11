package dk.apaq.simplepay.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author krog
 */
@Controller
public class CallbackController {
    
    @RequestMapping(value="/{publicKey}/{token}", method=RequestMethod.POST, params="gateway=quickpay") 
    public void handleQuickpayEvent(@PathVariable String publicKey, @PathVariable String token) {
        
    }
}

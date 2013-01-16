package dk.apaq.simplepay.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author krog
 */
@Controller
public class LoginController {
    
    @RequestMapping("/login")
    public String handleLogin() {
        return "login";
    }
}

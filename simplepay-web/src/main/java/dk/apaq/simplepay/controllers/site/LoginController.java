package dk.apaq.simplepay.controllers.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author krog
 */
@Controller
public class LoginController {
    
    @RequestMapping("/login.html")
    public String handleLogin() {
        return "login";
    }
    
    @RequestMapping("/signup.html")
    public String handleSignup() {
        return "signup";
    }
}

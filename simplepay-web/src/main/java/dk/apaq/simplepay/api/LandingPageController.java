package dk.apaq.simplepay.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author michael
 */
@Controller
public class LandingPageController {
    
    @RequestMapping("/index.htm")
    public String handleRequest() {
        return "landingpage";
    }
}

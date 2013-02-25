package dk.apaq.simplepay.controllers.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author michael
 */
@Controller
public class LandingPageController {
    
    @RequestMapping("/index.html")
    public String handleRequest() {
        return "landingpage";
    }
}

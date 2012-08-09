package dk.apaq.simplepay.teaser.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TeaserPage {
    
    @RequestMapping(value="/index.htm")
    public ModelAndView handleRequest() {
        return new ModelAndView("teaser");
    }
}

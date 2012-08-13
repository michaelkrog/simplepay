package dk.apaq.simplepay.teaser.site;

import dk.apaq.simplepay.teaser.MessageBundle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TeaserPage {
    
    @RequestMapping(value="/index.htm")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
        //This shouldnt be needed, but there seems to be a bug in spring-jade4j
        response.setCharacterEncoding("utf-8");
        
        MessageBundle message = MessageBundle.get(request.getLocale(), "dk.apaq.simplepay.teaser.i18n.Messages");
        
        String tooltip1 = message.get("tooltip1");
        String tooltip2 = message.get("tooltip2");
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("title", message.get("title"));
        model.put("text1", message.get("text1", tooltip1));
        model.put("text2", message.get("text2", tooltip2));
        model.put("button", message.get("button"));
        model.put("contact", message.get("contact"));
        model.put("example", message.get("example"));
        
        return new ModelAndView("teaser", model);
    }
}

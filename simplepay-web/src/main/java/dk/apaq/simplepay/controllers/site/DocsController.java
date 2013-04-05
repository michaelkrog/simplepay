
package dk.apaq.simplepay.controllers.site;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Javadoc
 */
@Controller
public class DocsController {

    private List<String> languages = Arrays.asList(new String[]{"en", "da"});
    private String defaultLanguage = "en";
    
    @RequestMapping(value = "/docs/{page}.html")
    public ModelAndView handleRequest(HttpServletRequest req, @PathVariable String page) {
        String language = req.getLocale().getLanguage();
        
        if(!languages.contains(language)) {
            language = defaultLanguage;
        }
        
        Map<String, String> model = new HashMap<String, String>();
        model.put("docfile", page);
        model.put("language", language);
        return new ModelAndView("doc", model);
    }
}

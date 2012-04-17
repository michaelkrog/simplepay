package dk.apaq.simplepay.site;



import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
public class ScriptController {
    
    @Autowired
    @Qualifier(value="publicUrl")
    private String publicUrl;
    
    @RequestMapping(value="/pay.js",method= RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getPayScript(HttpServletResponse response) {
        response.setContentType("application/javascript");
        return new ModelAndView("pay", "publicUrl", publicUrl);
    }
}

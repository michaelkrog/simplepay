/*
 * Copyright by Apaq 2011-2013
 */

package dk.apaq.simplepay.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Javadoc
 */
@Controller
public class ScriptController {

    @RequestMapping(value = "/pay.js", method= RequestMethod.GET)
    public ModelAndView handleRequest() {
        return new ModelAndView("pay");
    }
}

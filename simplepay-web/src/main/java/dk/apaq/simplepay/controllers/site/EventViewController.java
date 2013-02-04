package dk.apaq.simplepay.controllers.site;

import dk.apaq.simplepay.controllers.ControllerUtil;
import dk.apaq.simplepay.controllers.BaseController;

import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.framework.criteria.Sorter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
public class EventViewController extends BaseController {

    @Autowired
    private IPayService service;

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    @Secured("ROLE_MERCHANT")
    public ModelAndView listEvents(@RequestParam(required = false) String type, @RequestParam(required = false) String entityId, 
            @RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "1000") Integer limit) {
        Merchant m = ControllerUtil.getMerchant(service);

        Rule rule = null;
        if (entityId != null) {
            rule = Rules.equals("transaction.id", entityId);
        }

        return listEntities(service.getEvents(m, Event.class), rule, new Sorter("eventDate", Sorter.Direction.Descending), offset, limit, "events");

    }
}

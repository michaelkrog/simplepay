package dk.apaq.simplepay.api;

import java.util.List;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Limit;
import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.model.TransactionEvent;
import dk.apaq.simplepay.security.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author michael
 */
@Controller
public class EventController {

    @Autowired
    private IPayService service;

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public List listEvents(@RequestParam(required = false) String type, @RequestParam(required = false) String entityId) {
        Merchant m = SecurityHelper.getMerchant(service);

        Class clazz = null;
        if ("transaction".equals(type)) {
            clazz = TransactionEvent.class;
        }

        if ("token".equals(type)) {
            clazz = TokenEvent.class;
        }

        if (clazz == null) {
            clazz = Event.class;
        }

        Rule rule = null;
        if (entityId != null) {
            rule = Rules.equals("transaction.id", entityId);
        }

        List<Event> events = service.getEvents(m, clazz).findAll(new Criteria(rule, new Limit(15)));
        return events;

    }
}

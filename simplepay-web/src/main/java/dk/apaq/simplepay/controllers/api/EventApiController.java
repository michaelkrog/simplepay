package dk.apaq.simplepay.controllers.api;

import dk.apaq.simplepay.controllers.ControllerUtil;
import dk.apaq.simplepay.controllers.BaseController;
import java.util.List;

import dk.apaq.framework.criteria.Rule;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.framework.criteria.Sorter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.BaseEvent;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.TokenEvent;
import dk.apaq.simplepay.model.TransactionEvent;
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
public class EventApiController extends BaseController {

    @Autowired
    private IPayService service;

    @RequestMapping(value = "/events", method = RequestMethod.GET, headers = "Accept=application/json")
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public List listEvents(@RequestParam(required = false) String type, @RequestParam(required = false) String entityId, 
            @RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "1000") Integer limit) {
        Merchant m = ControllerUtil.getMerchant(service);

        Class clazz = null;
        if ("transaction".equals(type)) {
            clazz = TransactionEvent.class;
        }

        if ("token".equals(type)) {
            clazz = TokenEvent.class;
        }

        if (clazz == null) {
            clazz = BaseEvent.class;
        }

        Rule rule = null;
        if (entityId != null) {
            rule = Rules.equals("id", entityId);
        }

        return listEntities(service.getEvents(m, clazz), rule, new Sorter("eventDate", Sorter.Direction.Descending), offset, limit);

    }
}

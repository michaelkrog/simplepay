package dk.apaq.simplepay.api;

import dk.apaq.simplepay.security.SecurityHelper;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.limit.Limit;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.Event;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.TransactionEvent;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
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
    
    @RequestMapping(value="/events", method= RequestMethod.GET)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public List listEvents(@RequestParam(required = false) String type, @RequestParam(required = false) String entityId) {
        Merchant m = SecurityHelper.getMerchant(service);
        if ("transaction".equals(type)) {
            Filter filter = null;
            if(entityId != null) {
                filter = new CompareFilter("transaction.id", entityId, CompareFilter.CompareType.Equals);
            }
            return service.getEvents(m, TransactionEvent.class).list(filter, null, new Limit(15));
        }
        
        return null;
    }
}

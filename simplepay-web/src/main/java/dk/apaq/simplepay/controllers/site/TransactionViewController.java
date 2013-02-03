package dk.apaq.simplepay.controllers.site;

import dk.apaq.framework.criteria.Sorter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.controllers.BaseController;
import dk.apaq.simplepay.controllers.ControllerUtil;
import dk.apaq.simplepay.controllers.exceptions.ResourceNotFoundException;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author krog
 */
@Controller
public class TransactionViewController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionViewController.class);
    private final IPayService service;

    @Autowired
    public TransactionViewController(IPayService service) {
        this.service = service;
    }

    private Transaction getTransaction(Merchant m, String token) {
        Transaction t = service.getTransactions(m).findOne(token);
        if (t == null) {
            throw new ResourceNotFoundException("No transaction exists with the given token.");
        }
        return t;
    }

   
    /*
     
    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    public ModelAndView listTransactions(@RequestParam(required = false) String query, @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "1000") Integer limit) {
        Merchant m = ControllerUtil.getMerchant(service);
        return listEntities(service.getTransactions(m), query, new Sorter("dateCreated", Sorter.Direction.Descending), offset, limit, "transactions");
    }
    * */
}

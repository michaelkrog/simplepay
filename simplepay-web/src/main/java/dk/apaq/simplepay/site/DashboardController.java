package dk.apaq.simplepay.site;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Limit;
import dk.apaq.framework.criteria.Sorter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.data.ITransactionCrud;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
public class DashboardController {
    
    @Autowired
    private IPayService service;
    
    @RequestMapping("/dashboard/transactions")
    public ModelAndView showTransactions() {
        SystemUser user = service.getCurrentUser();
        SystemUser privateUser = service.getOrCreatePrivateUser(user.getMerchant());
        
        ITransactionCrud crud = service.getTransactions(user.getMerchant());
        List<Transaction> list =  crud.findAll(new Criteria(new Sorter("dateChanged", Sorter.Direction.Descending), new Limit(300)));
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("transactions", list);
        model.put("privateKey", privateUser.getUsername());
        return new ModelAndView("transactions", model);
    }
    
    @RequestMapping("/dashboard")
    public ModelAndView showDashboard() {
        return new ModelAndView("dashboard");
    }
    
    @RequestMapping("/dashboard/config")
    public ModelAndView shoConfig() {
        return new ModelAndView("config");
    }
}

package dk.apaq.simplepay.site;

import dk.apaq.filter.limit.Limit;
import dk.apaq.filter.sort.SortDirection;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author michael
 */
@Controller
public class TransactionController {
    
    @Autowired
    private IPayService service;
    
    @RequestMapping("/dashboard")
    public ModelAndView showTransactions() {
        SystemUser user = service.getCurrentUser();
        
        List<Transaction> list =  service.getTransactions(user.getMerchant()).list(null, new Sorter("dateChanged", SortDirection.Descending), new Limit(300));
        return new ModelAndView("transactions", "transactions", list);
    }
    
    /*@RequestMapping("/dashboard")
    public String showDashboard() {
        return "redirect:/dashboard/transactions";
    }*/
}

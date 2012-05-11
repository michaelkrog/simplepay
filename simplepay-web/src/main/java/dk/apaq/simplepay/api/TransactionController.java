package dk.apaq.simplepay.api;

import dk.apaq.simplepay.security.SecurityHelper;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.filter.core.OrFilter;
import dk.apaq.filter.sort.SortDirection;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.gateway.RemoteAuthPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionEvent;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author krog
 */
@Controller
public class TransactionController {
    
    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);
    private static final NumberFormat nfQuickPayOrderNumber = NumberFormat.getIntegerInstance();
    
    static {
        nfQuickPayOrderNumber.setGroupingUsed(false);
    }
    
    
    @Autowired
    private IPayService service;
    
    @Autowired
    private PaymentGatewayManager gatewayManager;
    
    @Autowired
    @Qualifier("publicUrl")
    private String publicUrl;
    
    private Transaction getTransaction(Merchant m, String token) {
        Transaction t = service.getTransactions(m).read(token);
        if(t == null) {
            throw new ResourceNotFoundException("No transaction exists with the given token.");
        }
        return t;
    }
  
    @RequestMapping(value = "/transactions" , method=RequestMethod.GET)
    @Transactional(readOnly=true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public List<Transaction> listTransactions(@RequestParam(required=false) TransactionStatus status, @RequestParam(required=false) String searchString,
                                                @RequestParam(required=false) Long beforeTimestamp, @RequestParam(required=false) Long afterTimestamp) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Listing transactions. [merchant={}]", m.getId());
        
        //There is a bug in the JPA-Filter code causing empty And-filter to create invalid HQL.
        //We make sure not to use an empty AndFilter
        boolean useFilter = status != null || searchString != null || beforeTimestamp != null || afterTimestamp != null;
        
        AndFilter filter = new AndFilter();
        if(status != null) {
            filter.addFilter(new CompareFilter("status", status, CompareFilter.CompareType.Equals));
        }
        
        if(searchString != null) {
            if(!searchString.endsWith("*")) {
                searchString = searchString + "*";
            }
            filter.addFilter(new OrFilter(
                    new LikeFilter("currency", searchString),
                    new LikeFilter("description", searchString),
                    new LikeFilter("orderNumber", searchString)
                    ));
        }
        
        if(beforeTimestamp != null) {
            filter.addFilter(new CompareFilter("dateCreated", new Date(beforeTimestamp), CompareFilter.CompareType.LessOrEqual));
        }
        
        if(afterTimestamp != null) {
            filter.addFilter(new CompareFilter("dateCreated", new Date(afterTimestamp), CompareFilter.CompareType.GreaterOrEqual));
        }
        
        Sorter sorter = new Sorter("dateCreated", SortDirection.Descending);
        
        return service.getTransactions(m).list(useFilter ? filter : null, sorter);
    }
    
    @RequestMapping(value="/transactions/{id}", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public Transaction getTransaction(@PathVariable String id) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Retrieving transaction. [merchant={};transaction={}]", m.getId(), id);
        return getTransaction(m, id);
    }
    
    @RequestMapping(value="/transactions/{id}/refund", method=RequestMethod.POST)
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public Transaction refundTransaction(HttpServletRequest request, @PathVariable String id, @RequestParam(required=false) Long amount) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Refunding transaction. [merchant={}; transaction={}; amount={}]", new Object[]{m.getId(), id, amount});
        Transaction t = getTransaction(m, id);
        
        if(amount == null) {
            amount = t.getCapturedAmount();
        }
        
        return service.getTransactions(m).refund(t, amount);
    }
    
    @RequestMapping(value="/transactions/{id}/charge", method=RequestMethod.POST)
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public Transaction chargeTransaction(HttpServletRequest request, @PathVariable String id, @RequestParam(required=false) Long amount) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Charging transaction. [merchant={}; transaction={}; amount={}]", new Object[]{m.getId(), id, amount});
        Transaction t = getTransaction(m, id);
        
        Token token = t.getToken();
        if(amount == null) {
            if(token.getAuthorizedAmount()>0) {
                amount = ((Token)token).getAuthorizedAmount();
            } else {
                throw new IllegalArgumentException("No amount specified");
            }
        }
        
        return service.getTransactions(m).charge(t, amount);
    }
    
    @RequestMapping(value="/transactions/{id}/cancel", method=RequestMethod.POST)
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public Transaction cancelTransaction(HttpServletRequest request, @PathVariable String id) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Cancelling transaction. [merchant={}; transaction={}]", m.getId(), id);
        Transaction t = getTransaction(m, id);
        return service.getTransactions(m).cancel(t);
    }
    
}

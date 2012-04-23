package dk.apaq.simplepay.api;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.filter.core.OrFilter;
import dk.apaq.filter.sort.SortDirection;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.gateway.PaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
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
    
    
    @RequestMapping(value = "/form", method=RequestMethod.POST)
    @Secured({"ROLE_PUBLICAPIACCESSOR","ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    @Transactional
    public PaymentGateway.FormData generateForm(HttpServletRequest request, String token, Long amount, String currency, String returnUrl, String cancelUrl) {
        Merchant m = ApiHelper.getMerchant(service);
        Transaction t = getTransaction(token);
        PaymentGatewayType gatewayType = t.getGatewayType();
        
        t.setCurrency(currency);
        service.getTransactions(m).update(t);
        
        SystemUser publicUser = service.getOrCreatePublicUser(m);
        String callbackUrl = publicUrl + "/api/callback/" + gatewayType.name().toLowerCase() + "/" + publicUser.getUsername() + "/" + t.getId();
        PaymentGateway gateway = gatewayManager.createPaymentGateway(m, gatewayType);
        return gateway.generateFormdata(t, amount, currency, returnUrl, cancelUrl, callbackUrl, request.getLocale());
    }

    @RequestMapping(value = "/transactions", method=RequestMethod.POST)
    @Transactional()
    @Secured({"ROLE_PUBLICAPIACCESSOR","ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public String createTransactions(HttpServletRequest request, @RequestParam String orderNumber, @RequestParam String description) {
        Merchant m = ApiHelper.getMerchant(service);
        LOG.debug("Creating transaction. [merchant={}; orderNumber={}]", m.getId(), orderNumber);
        
        
        Transaction transaction = new Transaction();
        transaction.setOrderNumber(orderNumber);
        transaction.setDescription(description);
        transaction.setGatewayType(m.getGatewayType());
        transaction = service.getTransactions(m).createAndRead(transaction);
        service.getEvents(m, TransactionEvent.class).create(new TransactionEvent(transaction, ApiHelper.getUsername(), TransactionStatus.New, request.getRemoteAddr()));
        return transaction.getId();
        
    }
    
    
    @RequestMapping(value = "/transactions" , method=RequestMethod.GET)
    @Transactional(readOnly=true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public List<Transaction> listTransactions(@RequestParam(required=false) TransactionStatus status, @RequestParam(required=false) String searchString,
                                                @RequestParam(required=false) Long beforeTimestamp, @RequestParam(required=false) Long afterTimestamp) {
        Merchant m = ApiHelper.getMerchant(service);
        LOG.debug("Listing transactions. [merchant={}]", m.getId());
        
        //There a bug in the JPA-Filter code causing empty And-filter to create invalid HQL.
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
    
    @RequestMapping(value="/transactions/{token}", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public Transaction getTransaction(@PathVariable String token) {
        Merchant m = ApiHelper.getMerchant(service);
        LOG.debug("Retrieving transaction. [merchant={};token={}]", m.getId(), token);
        return getTransaction(m, token);
    }
    
    @RequestMapping(value="/transactions/{token}/refund", method=RequestMethod.POST)
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public Transaction refundTransaction(HttpServletRequest request, @PathVariable String token, @RequestParam(required=false) Long amount) {
        Merchant m = ApiHelper.getMerchant(service);
        LOG.debug("Refunding transaction. [merchant={}; token={}; amount={}]", new Object[]{m.getId(), token, amount});
        Transaction t = getTransaction(m, token);
        
        if(amount == null) {
            amount = t.getAuthorizedAmount();
        }
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(m, t.getGatewayType());
        gateway.refund(t, amount);
        
        service.getEvents(m, TransactionEvent.class).create(new TransactionEvent(t, ApiHelper.getUsername(), TransactionStatus.Refunded, request.getRemoteAddr()));
        t.setStatus(TransactionStatus.Refunded);
        t.setRefundedAmount(amount);
        return service.getTransactions(m).update(t);
    }
    
    @RequestMapping(value="/transactions/{token}/charge", method=RequestMethod.POST)
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public Transaction chargeTransaction(HttpServletRequest request, @PathVariable String token, @RequestParam(required=false) Long amount) {
        Merchant m = ApiHelper.getMerchant(service);
        LOG.debug("Charging transaction. [merchant={}; token={}; amount={}]", new Object[]{m.getId(), token, amount});
        Transaction t = getTransaction(m, token);
        
        if(amount == null) {
            amount = t.getAuthorizedAmount();
        }
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(m, t.getGatewayType());
        gateway.capture(t, amount);
        
        service.getEvents(m, TransactionEvent.class).create(new TransactionEvent(t, ApiHelper.getUsername(), TransactionStatus.Captured, request.getRemoteAddr()));
        t.setStatus(TransactionStatus.Captured);
        t.setCapturedAmount(amount);
        return service.getTransactions(m).update(t);
    }
    
    @RequestMapping(value="/transactions/{token}/cancel", method=RequestMethod.POST)
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public Transaction cancelTransaction(HttpServletRequest request, @PathVariable String token) {
        Merchant m = ApiHelper.getMerchant(service);
        LOG.debug("Cancelling transaction. [merchant={}; token={}]", m.getId(), token);
        Transaction t = getTransaction(m, token);
        
        
        PaymentGateway gateway = gatewayManager.createPaymentGateway(m, t.getGatewayType());
        gateway.cancel(t);
        
        service.getEvents(m, TransactionEvent.class).create(new TransactionEvent(t, ApiHelper.getUsername(), TransactionStatus.Cancelled, request.getRemoteAddr()));
        t.setStatus(TransactionStatus.Cancelled);
        return service.getTransactions(m).update(t);
    }
    
}

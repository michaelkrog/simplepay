package dk.apaq.simplepay.api;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.framework.criteria.Sorter;
import dk.apaq.framework.criteria.rules.AndRule;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.security.SecurityHelper;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author krog
 */
@Controller
public class TransactionController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);
    private final IPayService service;

    @Autowired
    public TransactionController(IPayService service) {
        this.service = service;
    }

    private Transaction getTransaction(Merchant m, String token) {
        Transaction t = service.getTransactions(m).findOne(token);
        if (t == null) {
            throw new ResourceNotFoundException("No transaction exists with the given token.");
        }
        return t;
    }

    /* METHODS FOR JSON API. */
    
    @RequestMapping(value = "/transactions", method = RequestMethod.GET, headers = "Accept=application/json")
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public List<Transaction> listTransactionsAsJson(@RequestParam(required = false) String query, @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "1000") Integer limit) {
        Merchant m = ControllerUtil.getMerchant(service);
        return listEntities(service.getTransactions(m), query, new Sorter("dateCreated", Sorter.Direction.Descending), offset, limit);
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST, headers = "Accept=application/json")
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public String createTransaction(@RequestParam String token, @RequestParam String refId, @RequestParam String currency, @RequestParam Integer amount) {
        Merchant m = ControllerUtil.getMerchant(service);

        Token tokenObject = service.getTokens(m).findOne(token);
        if (tokenObject == null) {
            throw new ResourceNotFoundException("Token not found. ");
        }
        Money money = Money.ofMinor(CurrencyUnit.getInstance(currency), amount);
        return service.getTransactions(m).createNew(tokenObject, refId, money).getId();
    }

    @RequestMapping(value = "/transactions/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public Transaction getTransaction(@PathVariable String id) {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Retrieving transaction. [merchant={};transaction={}]", m.getId(), id);
        return getTransaction(m, id);
    }

    @RequestMapping(value = "/transactions/{id}/refund", method = RequestMethod.POST, headers = "Accept=application/json")
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public Transaction refundTransaction(HttpServletRequest request, @PathVariable String id, @RequestParam(required = false) Long amount) {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Refunding transaction. [merchant={}; transaction={}; amount={}]", new Object[]{m.getId(), id, amount});
        Transaction t = getTransaction(m, id);

        if (amount == null) {
            amount = t.getAmountCharged();
        }

        return service.getTransactions(m).refund(t, amount);
    }

    @RequestMapping(value = "/transactions/{id}/charge", method = RequestMethod.POST, headers = "Accept=application/json")
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public Transaction chargeTransaction(HttpServletRequest request, @PathVariable String id, @RequestParam(required = false) Long amount) {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Charging transaction. [merchant={}; transaction={}; amount={}]", new Object[]{m.getId(), id, amount});
        Transaction t = getTransaction(m, id);

        if (amount == null) {
            if (t.getAmount() > 0) {
                amount = t.getAmount();
            } else {
                throw new IllegalArgumentException("No amount specified");
            }
        }

        return service.getTransactions(m).charge(t, amount);
    }

    @RequestMapping(value = "/transactions/{id}/cancel", method = RequestMethod.POST, headers = "Accept=application/json")
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public Transaction cancelTransaction(HttpServletRequest request, @PathVariable String id) {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Cancelling transaction. [merchant={}; transaction={}]", m.getId(), id);
        Transaction t = getTransaction(m, id);
        return service.getTransactions(m).cancel(t);
    }

    /**
     * METHODS FOR VIEWS *
     */
    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public ModelAndView listTransactions(@RequestParam(required = false) String query, @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "1000") Integer limit) {
        Merchant m = ControllerUtil.getMerchant(service);
        return listEntities(service.getTransactions(m), query, new Sorter("dateCreated", Sorter.Direction.Descending), offset, limit, "transactions");
    }
}

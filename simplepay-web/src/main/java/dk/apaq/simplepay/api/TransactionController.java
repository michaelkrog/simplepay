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
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.security.SecurityHelper;
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
        Transaction t = service.getTransactions(m).findOne(token);
        if (t == null) {
            throw new ResourceNotFoundException("No transaction exists with the given token.");
        }
        return t;
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public List<Transaction> listTransactions(@RequestParam(required = false) ETransactionStatus status, @RequestParam(required = false) String searchString,
            @RequestParam(required = false) Long beforeTimestamp, @RequestParam(required = false) Long afterTimestamp) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Listing transactions. [merchant={}]", m.getId());

        boolean useRule = status != null || searchString != null || beforeTimestamp != null || afterTimestamp != null;

        AndRule rule = new AndRule();
        if (status != null) {
            rule.addRule(Rules.equals("status", status));
        }

        if (searchString != null) {
            if (!searchString.endsWith("*")) {
                searchString = searchString + "*";
            }
            rule.addRule(Rules.or(
                    Rules.like("currency", searchString),
                    Rules.like("description", searchString),
                    Rules.like("orderNumber", searchString)));
        }

        if (beforeTimestamp != null) {
            rule.addRule(Rules.lessOrEqual("dateCreated", new Date(beforeTimestamp)));
        }

        if (afterTimestamp != null) {
            rule.addRule(Rules.greaterOrEqual("dateCreated", new Date(afterTimestamp)));
        }

        Sorter sorter = new Sorter("dateCreated", Sorter.Direction.Descending);

        return service.getTransactions(m).findAll(new Criteria(useRule ? rule : null, sorter));
    }

    @RequestMapping(value = "/transactions/{id}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public Transaction getTransaction(@PathVariable String id) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Retrieving transaction. [merchant={};transaction={}]", m.getId(), id);
        return getTransaction(m, id);
    }

    @RequestMapping(value = "/transactions/{id}/refund", method = RequestMethod.POST)
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public Transaction refundTransaction(HttpServletRequest request, @PathVariable String id, @RequestParam(required = false) Long amount) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Refunding transaction. [merchant={}; transaction={}; amount={}]", new Object[]{m.getId(), id, amount});
        Transaction t = getTransaction(m, id);

        if (amount == null) {
            amount = t.getAmountCharged();
        }

        return service.getTransactions(m).refund(t, amount);
    }

    @RequestMapping(value = "/transactions/{id}/charge", method = RequestMethod.POST)
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public Transaction chargeTransaction(HttpServletRequest request, @PathVariable String id, @RequestParam(required = false) Long amount) {
        Merchant m = SecurityHelper.getMerchant(service);
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

    @RequestMapping(value = "/transactions/{id}/cancel", method = RequestMethod.POST)
    @Transactional
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public Transaction cancelTransaction(HttpServletRequest request, @PathVariable String id) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Cancelling transaction. [merchant={}; transaction={}]", m.getId(), id);
        Transaction t = getTransaction(m, id);
        return service.getTransactions(m).cancel(t);
    }
}

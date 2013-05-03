package dk.apaq.simplepay.controllers.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.apaq.framework.criteria.Sorter;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.controllers.BaseController;
import dk.apaq.simplepay.controllers.ControllerUtil;
import dk.apaq.simplepay.controllers.exceptions.ResourceNotFoundException;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import org.apache.commons.lang.Validate;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author krog
 */
@Controller
public class TransactionApiController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionApiController.class);
    private final IPayService service;
    private final RestErrorHandler errorHandler;

    @Autowired
    public TransactionApiController(IPayService service, RestErrorHandler errorHandler) {
        this.service = service;
        this.errorHandler = errorHandler;
    }

    private Transaction getTransaction(Merchant m, String token) {
        Transaction t = service.getTransactions(m).findOne(token);
        if (t == null) {
            throw new ResourceNotFoundException("No transaction exists with the given token.");
        }
        return t;
    }

    @ExceptionHandler(Throwable.class)
    public void handleException(Throwable ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        errorHandler.handleThrowable(request, response, ex);
    }
        
    /**
     * List transactions for the current merchant.
     * @param query The query in Simple Query Format
     * @param offset Offset in the result.
     * @param limit Max numbers of results.
     * @return The transactions.
     */
    @RequestMapping(value = "/transactions", method = RequestMethod.GET, headers = "Accept=application/json")
    @Transactional(readOnly = true)
    @ResponseBody
    public Iterable<Transaction> listTransactions(@RequestParam(required = false) String query, @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "1000") Integer limit) {
        Merchant m = ControllerUtil.getMerchant(service);
        return listEntities(service.getTransactions(m), query, new Sorter("dateCreated", Sorter.Direction.Descending), offset, limit);
    }

    /**
     * Creates a new transaction for the current merchant. 
     * The new transaction will only be created if it can be authorized by a backing merchant gateway.
     * @param token The token to use for the transaction.
     * @param refId The reference id, fx. order id
     * @param currency The current as 3-letter currencycode
     * @param amount The amount in minors. (Fx. 100.00 would be 10000)
     * @return The id of the new transaction
     */
    @RequestMapping(value = "/transactions", method = RequestMethod.POST, headers = "Accept=application/json")
    @Transactional(readOnly = true)
    @ResponseBody
    public Transaction createTransaction(@RequestParam String token, @RequestParam String refId, @RequestParam String currency, 
                                    @RequestParam Long amount) {
        Validate.isTrue(amount > 0, "Amount cannot be '0'.");
        Merchant m = ControllerUtil.getMerchant(service);
        Money money = Money.ofMinor(CurrencyUnit.getInstance(currency), amount);
        return service.getTransactions(m).createNew(token, refId, money);
    }

    /**
     * Gets a specific transaction.
     * @param id The id of the transaction.
     * @return The transaction
     */
    //@RequestMapping(value = "/transactions/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT" })
    @ResponseBody
    public Transaction getTransaction(@PathVariable String id) {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Retrieving transaction. [merchant={};transaction={}]", m.getId(), id);
        return getTransaction(m, id);
    }

    /**
     * Refunds a charged transaction.
     * @param id The id of the transaction.
     * @param amount The amount to refund in minors (Fx. 100.00 would be 10000. )
     * @return The transaction.
     */
    @RequestMapping(value = "/transactions/{id}/refund", method = RequestMethod.POST, headers = "Accept=application/json")
    @Transactional
    @ResponseBody
    public Transaction refundTransaction(@PathVariable String id, @RequestParam(required = false) Long amount) {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Refunding transaction. [merchant={}; transaction={}; amount={}]", new Object[]{m.getId(), id, amount});
        Transaction t = getTransaction(m, id);

        if (amount == null) {
            amount = t.getAmountCharged();
        }

        return service.getTransactions(m).refund(t, amount);
    }

    /**
     * Charges an authorized transaction.
     * @param id The id of the transaction.
     * @param amount The amount to charge in minors (Fx. 100.00 would be 10000. )
     * @return The transaction.
     */
    @RequestMapping(value = "/transactions/{id}/charge", method = RequestMethod.POST, headers = "Accept=application/json")
    @Transactional
    @ResponseBody
    public Transaction chargeTransaction(@PathVariable String id, @RequestParam(required = false) Long amount) {
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

    /**
     * Cancels an authorized transaction.
     * @param id The id of the transaction.
     * @return The transaction.
     */
    @RequestMapping(value = "/transactions/{id}/cancel", method = RequestMethod.POST, headers = "Accept=application/json")
    @Transactional
    @ResponseBody
    public Transaction cancelTransaction(@PathVariable String id) {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Cancelling transaction. [merchant={}; transaction={}]", m.getId(), id);
        Transaction t = getTransaction(m, id);
        return service.getTransactions(m).cancel(t);
    }


}

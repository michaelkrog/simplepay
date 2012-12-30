package dk.apaq.simplepay.api;

import java.util.List;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.model.*;
import dk.apaq.simplepay.security.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author krog
 */
@Controller
public class TokenController {

    private static final Logger LOG = LoggerFactory.getLogger(TokenController.class);
    @Autowired
    private IPayService service;
    @Autowired
    private PaymentGatewayManager gatewayManager;
    @Autowired
    @Qualifier("publicUrl")
    private String publicUrl;

    private Token getToken(Merchant m, String token) {
        Token t = service.getTokens(m).findOne(token);
        if (t == null) {
            throw new ResourceNotFoundException("No token exists with the given token id.");
        }
        return t;
    }

    @RequestMapping(value = "/tokens", method = RequestMethod.POST)
    @Transactional()
    @Secured({"ROLE_PUBLICAPIACCESSOR", "ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT" })
    @ResponseBody
    public String createToken(@RequestParam String cardNumber, @RequestParam int expireMonth, @RequestParam int expireYear,
            @RequestParam String cvd) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Creating token. [merchant={}]", m.getId());
        Card card = new Card(cardNumber, expireMonth, expireYear, cvd);
        return service.getTokens(m).createNew(card).getId();
    }

    @RequestMapping(value = "/tokens", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT" })
    @ResponseBody
    public List<Token> listTokens() {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Listing tokens. [merchant={}]", m.getId());
        return service.getTokens(m).findAll();
    }

    @RequestMapping(value = "/tokens/{token}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT" })
    @ResponseBody
    public Token getToken(@PathVariable String token) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Retrieving token. [merchant={};token={}]", m.getId(), token);
        return getToken(m, token);
    }


}

package dk.apaq.simplepay.controllers.api;

import java.util.List;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.controllers.ControllerUtil;
import dk.apaq.simplepay.controllers.exceptions.ResourceNotFoundException;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TokenApiController {

    private static final Logger LOG = LoggerFactory.getLogger(TokenApiController.class);
    private final IPayService service;
    
    @Autowired
    public TokenApiController(IPayService service) {
        this.service = service;
    }
    
    private Token getToken(Merchant m, String token) {
        Token t = service.getTokens(m).findOne(token);
        if (t == null) {
            throw new ResourceNotFoundException("No token exists with the given token id.");
        }
        return t;
    }

    /**
     * Creates a new token based on the given parameters.
     * @param cardNumber The card number.
     * @param expireMonth The expiration month(1-12) 
     * @param expireYear The expiration year(fx. 2015) 
     * @param cvd The cvd code.
     * @return The token id.
     */
    //@RequestMapping(value = "/tokens", method = RequestMethod.POST, headers = "Accept=application/json")
    @Transactional()
    @Secured({"ROLE_PUBLICAPIACCESSOR", "ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT" }) 
    @ResponseBody
    public String createToken(@RequestParam String cardNumber, @RequestParam int expireMonth, @RequestParam int expireYear,
            @RequestParam String cvd) {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Creating token. [merchant={}]", m);
        Card card = new Card(cardNumber, expireYear, expireMonth, cvd);
        return service.getTokens(m).createNew(card).getId();
    }

    /**
     * List tokens for the current merchant.
     * @return The tokens.
     */
    //@RequestMapping(value = "/tokens", method = RequestMethod.GET, headers = "Accept=application/json")
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT" })
    @ResponseBody
    public List<Token> listTokens() {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Listing tokens. [merchant={}]", m.getId());
        return service.getTokens(m).findAll();
    }

    /**
     * Gets a specific token.
     * @param token The token id.
     * @return The token.
     */
    //@RequestMapping(value = "/tokens/{token}", method = RequestMethod.GET, headers = "Accept=application/json")
    @Transactional(readOnly = true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT" })
    @ResponseBody
    public Token getToken(@PathVariable String token) {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Retrieving token. [merchant={};token={}]", m.getId(), token);
        return getToken(m, token);
    }


}

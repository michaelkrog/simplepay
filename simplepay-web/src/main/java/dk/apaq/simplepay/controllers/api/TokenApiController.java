package dk.apaq.simplepay.controllers.api;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.controllers.ControllerUtil;
import dk.apaq.simplepay.controllers.exceptions.ParameterException;
import dk.apaq.simplepay.controllers.exceptions.ResourceNotFoundException;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author krog
 */
@Controller
public class TokenApiController {

    private static final Logger LOG = LoggerFactory.getLogger(TokenApiController.class);
    private final IPayService service;
    private final StringEncryptor encryptor;
    private final RestErrorHandler errorHandler;

    @Autowired
    public TokenApiController(IPayService service, StringEncryptor encryptor, RestErrorHandler errorHandler) {
        this.service = service;
        this.encryptor = encryptor;
        this.errorHandler = errorHandler;
    }

    private Token getToken(Merchant m, String token) {
        Token t = service.getTokens(m).findOne(token);
        if (t == null) {
            throw new ResourceNotFoundException("No token exists with the given token id.");
        }
        return t;
    }

    @ExceptionHandler(Throwable.class)
    public void handleException(Throwable ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        errorHandler.handleThrowable(request, response, ex);
    }

    /**
     * Creates a new token based on the given parameters.
     *
     * @param cardNumber The card number.
     * @param expireMonth The expiration month(1-12)
     * @param expireYear The expiration year(fx. 2015)
     * @param cvd The cvd code.
     * @return The token id.
     */
    @RequestMapping(value = "/tokens", method = RequestMethod.POST, headers = "Accept=application/json")
    @Transactional()
    @ResponseBody
    public Token createToken(@RequestParam String cardNumber, @RequestParam int expireMonth, @RequestParam int expireYear,
            @RequestParam String cvd) {
        validateAgainstPattern(Card.PATTERN_CARDNUMBER, trimSpaces(cardNumber), "cardNumber", "cardNumber must consist of 12-16 digits.");
        validateNumberRanges(new int[]{10, 2010}, new int[]{99, 2099}, expireYear, "expireYear");
        validateNumberRange(1, 12, expireMonth, "expireMonth");
        validateAgainstPattern(Card.PATTERN_CVD, cvd, "cvd", "cvd must consist of 3 or 4 digits.");
        
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Creating token. [merchant={}]", m);
        Card card = new Card(cardNumber, expireYear, expireMonth, cvd, encryptor);
        
        if(!card.isValid()) {
            throw new ParameterException("cardNumber given is not a valid card number.", "cardNumber");
        }
        
        return service.getTokens(m).createNew(card);
    }

    /**
     * List tokens for the current merchant.
     *
     * @return The tokens.
     */
    @RequestMapping(value = "/tokens", method = RequestMethod.GET, headers = "Accept=application/json")
    @Transactional(readOnly = true)
    @ResponseBody
    public Iterable<Token> listTokens() {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Listing tokens. [merchant={}]", m.getId());
        return service.getTokens(m).findAll();
    }

    /**
     * Gets a specific token.
     *
     * @param token The token id.
     * @return The token.
     */
    @RequestMapping(value = "/tokens/{token}", method = RequestMethod.GET, headers = "Accept=application/json")
    @Transactional(readOnly = true)
    @ResponseBody
    public Token getToken(@PathVariable String token) {
        Merchant m = ControllerUtil.getMerchant(service);
        LOG.debug("Retrieving token. [merchant={};token={}]", m.getId(), token);
        return getToken(m, token);
    }
    
    private String trimSpaces(String text) {
        return text.replace(" ", "");
    }
    
    private void validateAgainstPattern(Pattern pattern, String text, String parameterName) {
        validateAgainstPattern(pattern, text, parameterName, null);
    }
    
    private void validateAgainstPattern(Pattern pattern, String text, String parameterName, String message) {
        Matcher m = pattern.matcher(text);
        if(!m.matches()) {
            if(message == null) {
                message = m.toString();
            }
            throw new ParameterException(message, parameterName);
        }
    }
    
    private void validateNumberRange(int min, int max, int number, String parameterName) {
        if(number < min || number > max) {
            throw new ParameterException(parameterName + " must be within " + min + "-" + max + ".", parameterName);
        }
    }
    
    private void validateNumberRanges(int min[], int max[], int number, String parameterName) {
        boolean withinRanges = false;
        int numberOfRanges = Math.min(min.length, max.length);
        
        for(int i=0;i<numberOfRanges; i++) {
            if(number >= min[i] && number<= max[i]) {
                withinRanges = true;
                break;
            }
        } 
        
        if(!withinRanges) {
            StringBuilder sb = new StringBuilder();
            sb.append(parameterName).append(" must be within ");
            for(int i=0;i<numberOfRanges; i++) {
                if(i>0) {
                    if(i==numberOfRanges-1) {
                        sb.append(" or ");
                    } else {
                        sb.append(", ");
                    }
                }
                sb.append(min[i]).append("-").append(max[i]);
            } 
            sb.append(".");
            throw new ParameterException(sb.toString(), parameterName);
        }
    }
}

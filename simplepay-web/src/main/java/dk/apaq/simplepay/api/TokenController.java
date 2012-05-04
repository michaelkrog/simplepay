package dk.apaq.simplepay.api;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.gateway.RemoteAuthPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.security.SecurityHelper;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
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
        Token t = service.getTokens(m).read(token);
        if(t == null) {
            throw new ResourceNotFoundException("No token exists with the given token id.");
        }
        return t;
    }
    
    @RequestMapping(value = "/form", method=RequestMethod.POST)
    @Secured({"ROLE_PUBLICAPIACCESSOR","ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    @Transactional
    public RemoteAuthPaymentGateway.FormData generateForm(HttpServletRequest request, String token, Long amount, String currency, String returnUrl, String cancelUrl) {
        Merchant m = SecurityHelper.getMerchant(service);
        Token t = getToken(token);
        
        if(!(t instanceof Token)) {
            throw new IllegalArgumentException("The token specificed is not a token for remote authorization.");
        }
        
        PaymentGatewayType gatewayType = t.getGatewayType();
        
        SystemUser publicUser = service.getOrCreatePublicUser(m);
        String callbackUrl = publicUrl + "/api/callback/" + gatewayType.name().toLowerCase() + "/" + publicUser.getUsername() + "/" + t.getId();
        RemoteAuthPaymentGateway gateway = gatewayManager.createPaymentGateway(m, gatewayType);
        return gateway.generateFormdata(t, amount, currency, returnUrl, cancelUrl, callbackUrl, request.getLocale());
    }
    
    @RequestMapping(value = "/tokens", method=RequestMethod.POST)
    @Transactional()
    @Secured({"ROLE_PUBLICAPIACCESSOR","ROLE_PRIVATEAPIACCESSOR", "ROLE_MERCHANT"})
    @ResponseBody
    public String createToken(HttpServletRequest request, @RequestParam(required=false) String description) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Creating token. [merchant={}]", m.getId());
        //TODO Implement support for tokens that are not authorized remotely.
        return service.getTokens(m).createNew(m.getGatewayType(), description).getId();
    }
    
    
    @RequestMapping(value = "/transactions" , method=RequestMethod.GET)
    @Transactional(readOnly=true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public List<Token> listTokens() {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Listing transactions. [merchant={}]", m.getId());
        return service.getTokens(m).list();
    }
    
    @RequestMapping(value="/transactions/{token}", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    @Secured({"ROLE_PRIVATEAPIACCESSOR","ROLE_MERCHANT"})     
    @ResponseBody
    public Token getToken(@PathVariable String token) {
        Merchant m = SecurityHelper.getMerchant(service);
        LOG.debug("Retrieving transaction. [merchant={};token={}]", m.getId(), token);
        return getToken(m, token);
    }
}

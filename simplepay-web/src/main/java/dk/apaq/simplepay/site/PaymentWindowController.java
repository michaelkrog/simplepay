package dk.apaq.simplepay.site;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.security.SecurityHelper;
import dk.apaq.simplepay.common.EPaymentMethod;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
public class PaymentWindowController {
    
    @Autowired
    private IPayService service;
    

    
    @RequestMapping(value="/paymentwindow", method=RequestMethod.POST)
    public ModelAndView showPaymentWindow(@RequestParam String token, @RequestParam String publicKey, @RequestParam long amount, @RequestParam String currency, @RequestParam String returnUrl, @RequestParam String cancelUrl) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("token", token);
        model.put("amount", amount);
        model.put("currency", currency);
        model.put("returnUrl", returnUrl);
        model.put("cancelUrl", cancelUrl);
        model.put("publicKey", publicKey);
        return new ModelAndView("paymentwindow", model);
    }
    
    @RequestMapping(value="/paymentwindow/handle", method=RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String handlePaymentWindow(HttpServletRequest request, @RequestParam(value="token") String tokenId, @RequestParam String publicKey, 
                                        @RequestParam long amount, @RequestParam String currency, String cardNumber, String cvc, int expireMonth, int expireYear) {
        SystemUser user = service.getUser(publicKey);
        Merchant merchant = user.getMerchant();
        
        //TODO Detect payment method
        EPaymentMethod paymentMethod = null;
        
        /*Token token = service.getTokens(merchant).read(tokenId);
        token = service.getTokens(merchant).authorize(token, currency, amount, paymentMethod, cardNumber, cvc, expireMonth, expireYear);
        service.getTransactions(merchant).createNew(token);
            */
        return "OK";
    }
}

package dk.apaq.simplepay.security;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author michael
 */
public class SecurityHelper {
    
    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static Merchant getMerchant(IPayService service) {
        String username = getUsername();
        SystemUser user = service.getUser(username);
        return user.getMerchant();
    }
    
    public static boolean isAnonymousUser() {
        return SecurityContextHolder.getContext().getAuthentication() == null || "anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}

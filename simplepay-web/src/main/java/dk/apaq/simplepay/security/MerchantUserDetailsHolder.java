package dk.apaq.simplepay.security;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author krog
 */
public class MerchantUserDetailsHolder {
    
    public static MerchantUserDetails getMerchantUserDetails() {
        return (MerchantUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

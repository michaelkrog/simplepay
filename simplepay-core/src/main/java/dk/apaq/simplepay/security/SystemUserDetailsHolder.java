package dk.apaq.simplepay.security;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author krog
 */
public class SystemUserDetailsHolder {
    
    public static SystemUserDetails getDetails() {
        return (SystemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

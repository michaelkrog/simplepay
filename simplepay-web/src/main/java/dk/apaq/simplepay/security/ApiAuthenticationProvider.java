package dk.apaq.simplepay.security;

import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.model.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author krog
 */
public class ApiAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PayService service;
    
    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) a;
        
        Merchant m = service.getMerchantBySecretKey(token.getName());
        if(m==null) {
            throw new BadCredentialsException("Invalid secrect key.");
        }
        
        MerchantUserDetails mud = new MerchantUserDetails(m);
        return createSuccessfulAuthentication(mud, token);
    }

    @Override
    public boolean supports(Class<?> type) {
        return type == UsernamePasswordAuthenticationToken.class;
    }
    
    protected Authentication createSuccessfulAuthentication(UserDetails userDetails, UsernamePasswordAuthenticationToken auth) {
        UsernamePasswordAuthenticationToken successAuth = new UsernamePasswordAuthenticationToken(userDetails, auth.getCredentials(), userDetails.getAuthorities());
        successAuth.setDetails(auth.getDetails());
        return successAuth;
    }
    
}

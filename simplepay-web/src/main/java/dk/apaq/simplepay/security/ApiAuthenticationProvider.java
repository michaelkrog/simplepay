package dk.apaq.simplepay.security;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.model.Merchant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author krog
 */
public class ApiAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private IPayService service;
    
    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        /*UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) a;
        
        Merchant m = service.getMerchantBySecretKey(token.getName());
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        
        if(m!=null) {
            authList.add(new SimpleGrantedAuthority("ROLE_PRIVATE"));
            authList.add(new SimpleGrantedAuthority("ROLE_PUBLIC"));
        } else {
            m = service.getMerchantByPublicKey(token.getName());
            authList.add(new SimpleGrantedAuthority("ROLE_PUBLIC"));
        }
        
        if(m==null) {
            throw new BadCredentialsException("Invalid key.");
        }
        
        SystemUserDetails mud = new SystemUserDetails(m);
        mud.getAuthorities().addAll(authList);
        
        return createSuccessfulAuthentication(mud, token);*/
        return null;
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

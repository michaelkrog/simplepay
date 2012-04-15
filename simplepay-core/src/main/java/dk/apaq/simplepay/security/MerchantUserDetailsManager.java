package dk.apaq.simplepay.security;

import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.model.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 *
 * @author krog
 */
public class MerchantUserDetailsManager implements UserDetailsManager {

    @Autowired
    private PayService service;
    
    @Override
    public void createUser(UserDetails user) {
        MerchantUserDetails mud = (MerchantUserDetails) user;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateUser(UserDetails user) {
        MerchantUserDetails mud = (MerchantUserDetails) user;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteUser(String username) {
        Merchant m = service.getMerchantByUsername(username);
        if(m!=null) {
            service.getMerchants().delete(m.getId());
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean userExists(String username) {
        return service.getMerchantByUsername(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Merchant m = service.getMerchantByUsername(username);
        if(m==null) {
            throw new UsernameNotFoundException("User not found. [username="+username+"]");
        }
        
        return new MerchantUserDetails(m);
    }
    
}

package dk.apaq.simplepay.security;

import dk.apaq.simplepay.PayService;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 *
 * @author krog
 */
public class SystemUserDetailsManager implements UserDetailsManager {

    @Autowired
    private PayService service;
    
    @Override
    public void createUser(UserDetails user) {
        SystemUserDetails mud = (SystemUserDetails) user;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateUser(UserDetails user) {
        SystemUserDetails mud = (SystemUserDetails) user;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteUser(String username) {
        SystemUser user = service.getUser(username);
        if(user!=null) {
            service.getUsers().delete(user.getId());
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean userExists(String username) {
        return service.getUser(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser user = service.getUser(username);
        if(user==null) {
            throw new UsernameNotFoundException("User not found. [username="+username+"]");
        }
        
        return new SystemUserDetails(user);
    }
    
}

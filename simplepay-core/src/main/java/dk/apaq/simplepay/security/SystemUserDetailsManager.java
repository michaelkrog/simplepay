package dk.apaq.simplepay.security;

import java.util.ArrayList;
import java.util.List;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author krog
 */
public class SystemUserDetailsManager implements UserDetailsService {

    private final IPayService service;

    @Autowired
    public SystemUserDetailsManager(IPayService service) {
        this.service = service;
    }

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser user = service.getUserService().getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found. [username=" + username + "]");
        }

        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        for (ERole role : user.getRoles()) {
            authList.add(new SimpleGrantedAuthority("ROLE_" + role.name().toUpperCase()));
        }
        return new User(user.getUsername(), user.getPassword(), !user.isDisabled(), !user.isExpired(), !user.isCredentialsExpired(), !user.isLocked(), authList);
    }
}

package dk.apaq.simplepay.security;

import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Role;
import dk.apaq.simplepay.model.SystemUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author krog
 */
public class SystemUserDetails implements UserDetails {

    private final SystemUser user;
    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    public SystemUserDetails(SystemUser user) {
        this.user = user;
        
        for(Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name().toUpperCase()));
        }
    }

    public SystemUser getUser() {
        return user;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.isExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return !user.isDisabled();
    }
    
}

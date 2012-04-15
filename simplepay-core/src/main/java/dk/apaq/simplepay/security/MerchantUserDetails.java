package dk.apaq.simplepay.security;

import dk.apaq.simplepay.model.Merchant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author krog
 */
public class MerchantUserDetails implements UserDetails {

    private final Merchant merchant;
    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    public MerchantUserDetails(Merchant merchant) {
        this.merchant = merchant;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return merchant.getPassword();
    }

    @Override
    public String getUsername() {
        return merchant.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !merchant.isExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !merchant.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !merchant.isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return !merchant.isDisabled();
    }
    
}

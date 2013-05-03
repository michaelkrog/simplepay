package dk.apaq.simplepay.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import dk.apaq.simplepay.security.ERole;
import org.springframework.data.domain.Persistable;

/**
 *
 * @author michael
 */
public class SystemUser implements Serializable, Persistable<String> {

    private String id;
    @NotNull
    private String username;
    private String password;
    private boolean disabled;
    private boolean expired;
    private boolean credentialsExpired;
    private boolean locked;
    @NotNull
    private Merchant merchant;
    private List<ERole> roles = new ArrayList<ERole>();

    public SystemUser() {
    }

    public SystemUser(Merchant merchant, String username, String password, ERole... roles) {
        this.username = username;
        this.password = password;
        this.merchant = merchant;
        this.roles.addAll(Arrays.asList(roles));
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ERole> getRoles() {
        return roles;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
    
    
}

package dk.apaq.simplepay.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

/**
 * Specifies a user's access to a merchant.
 */
@Entity
public class MerchantAccess extends BaseEntity {

    private String username;
    @ElementCollection
    private List<String> roles = new ArrayList<String>();

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

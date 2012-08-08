package dk.apaq.simplepay.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;

/**
 *
 * @author krog
 */
@Entity
public class MerchantAccess extends BaseEntity {
    private String username;
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

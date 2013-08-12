
package dk.apaq.simplepay.service;

import dk.apaq.simplepay.repository.ISystemUserRepository;
import dk.apaq.simplepay.model.SystemUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Javadoc
 */
public class UserService extends BaseService<SystemUser, String> {

    private ISystemUserRepository repository;
    
    public SystemUser getUser(String username) {
        return repository.findByUsername(username);
    }

    public String getCurrentUsername() {
        SystemUser user = getCurrentUser();
        return user == null ? "Anonymous" : user.getUsername();
    }

    public SystemUser getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUser(username);
    }

}

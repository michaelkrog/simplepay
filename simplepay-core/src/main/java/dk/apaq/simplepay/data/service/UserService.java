
package dk.apaq.simplepay.data.service;

import dk.apaq.simplepay.data.ISystemUserRepository;
import dk.apaq.simplepay.model.SystemUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Javadoc
 */
public class UserService implements IUserService {

    private ISystemUserRepository repository;
    
    @Override
    public SystemUser getUser(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public String getCurrentUsername() {
        SystemUser user = getCurrentUser();
        return user == null ? "Anonymous" : user.getUsername();
    }

    @Override
    public SystemUser getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUser(username);
    }

}

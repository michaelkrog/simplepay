package dk.apaq.simplepay.service;

import dk.apaq.simplepay.repository.ISystemUserRepository;
import dk.apaq.simplepay.model.SystemUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Service for retrieving users.<br>
 * <br>
 * The service will in turn use a repository set by the system, but offers only methods that are to be exposed via extarnal interface.
 */
public class UserService extends BaseService<SystemUser, ISystemUserRepository> {

    private CurrentUserResolver userResolver = new DefaultUserResolver();

    public interface CurrentUserResolver {

        String resolveUser();
    }

    public class DefaultUserResolver implements CurrentUserResolver {

        @Override
        public String resolveUser() {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                return null;
            }
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
    }

    public void setUserResolver(CurrentUserResolver userResolver) {
        this.userResolver = userResolver;
    }

    /**
     * Retrieves a user with the given username.
     *
     * @return The user or null if no user exists with the given username.
     */
    public SystemUser getUser(String username) {
        return repository.findByUsername(username);
    }

    /**
     * Retreives the username of the current user related to this Thread.
     *
     * @return The username or 'Anonymous' if no user related to this thread.
     */
    public String getCurrentUsername() {
        SystemUser user = getCurrentUser();
        return user == null ? "Anonymous" : user.getUsername();
    }

    /**
     * Retreives the user related to this Thread.
     *
     * @return The user or null if no user related.
     */
    public SystemUser getCurrentUser() {
        return getCurrentUser(false);
    }

    public SystemUser getCurrentUser(boolean require) {
        String username = userResolver.resolveUser();
        SystemUser user = getUser(username);
        if (user == null && require) {
            throw new SecurityException("No current user found.");
        }
        return user;
    }
}

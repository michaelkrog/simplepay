package dk.apaq.simplepay.data.service;

import dk.apaq.simplepay.model.SystemUser;

/**
 *
 * @author krog
 */
public interface IUserService {
     public SystemUser getUser(String username);
     public String getCurrentUsername();
     public SystemUser getCurrentUser();
}

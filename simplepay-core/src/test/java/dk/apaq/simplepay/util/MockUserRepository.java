
package dk.apaq.simplepay.util;

import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.repository.ISystemUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Javadoc
 */
public class MockUserRepository extends MockRepositoryBase<SystemUser> implements ISystemUserRepository {

    @Override
    public SystemUser findByUsername(String username) {
        for(SystemUser user : findAll()) {
            if(username.equals(user.getUsername())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Iterable<SystemUser> findAll(Sort sort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<SystemUser> findAll(Pageable pgbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

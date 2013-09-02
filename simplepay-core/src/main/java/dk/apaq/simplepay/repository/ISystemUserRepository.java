
package dk.apaq.simplepay.repository;

import dk.apaq.simplepay.model.SystemUser;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Javadoc
 */
public interface ISystemUserRepository extends PagingAndSortingRepository<SystemUser, String> {

    SystemUser findByUsername(String username);
}
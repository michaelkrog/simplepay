package dk.apaq.simplepay.service;

import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.repository.ISystemUserRepository;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mockito;

public class UserServiceTest {
    
    
    private UserService service;
    private ISystemUserRepository repository;
    private SystemUser user;
    
    @Before
    public void init() {
        user = new SystemUser();
        user.setUsername("john");
        
        repository = Mockito.mock(ISystemUserRepository.class);
        Mockito.when(repository.findByUsername(Mockito.eq(user.getUsername()))).thenReturn(user);
        
        service = new UserService();
        service.setRepository(repository);
        service.setUserResolver(new UserService.CurrentUserResolver() {
            public String resolveUser() {
                return user.getUsername();
            }
        });
    }
    
    @Test
    public void testGetUser() {
        String username = user.getUsername();
        SystemUser result = service.getUser(username);
        assertEquals(user, result);
    }

    @Test
    public void testGetCurrentUsername() {
        String result = service.getCurrentUsername();
        assertEquals(user.getUsername(), result);
    }

    @Test
    public void testGetCurrentUser() {
        SystemUser result = service.getCurrentUser();
        assertEquals(user, result);
    }
}
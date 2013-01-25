package dk.apaq.simplepay.security;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author krog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
@Transactional
public class SystemUserDetailsManagerTest {
    
    @Autowired
    private IPayService service;

    /**
     * Test of loadUserByUsername method, of class SystemUserDetailsManager.
     */
    @Test
    public void testLoadUserByUsername() {
        System.out.println("loadUserByUsername");
        
        Merchant m = service.getMerchants().save(new Merchant());
        SystemUser user = new SystemUser(m, "john", "doe", ERole.Merchant);
        
        service.getUsers().save(user);
        
        SystemUserDetailsManager instance = new SystemUserDetailsManager(service);
        UserDetails result = instance.loadUserByUsername("john");
        assertEquals("ROLE_MERCHANT", result.getAuthorities().iterator().next().getAuthority());
        
        
    }
}

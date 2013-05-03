package dk.apaq.simplepay.security;

import java.util.Collection;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author krog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/defaultspringcontext.xml"})
public class SystemUserDetailsManagerTest {

    @Autowired
    private IPayService service;

    /**
     * Test of loadUserByUsername method, of class SystemUserDetailsManager.
     */
    @Test
    public void testLoadUserByUsername() throws InterruptedException {
        System.out.println("loadUserByUsername");

        String username = "U_" + System.currentTimeMillis();
        Merchant m = service.getMerchants().save(new Merchant());
        SystemUser user = new SystemUser(m, username, "doe", ERole.Merchant);

        user = service.getUsers().save(user);
        assertEquals(1, user.getRoles().size());

        //Thread.sleep(100);
        user = service.getUsers().findOne(user.getId());
        assertEquals(1, user.getRoles().size());

        SystemUserDetailsManager instance = new SystemUserDetailsManager(service);
        UserDetails result = instance.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> auths = result.getAuthorities();
        Object[] authArray = auths.toArray();

        assertEquals(1, authArray.length);

        GrantedAuthority auth = (GrantedAuthority) authArray[0];
        assertEquals("ROLE_MERCHANT", auth.getAuthority());


    }
}

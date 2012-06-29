package dk.apaq.simplepay.model;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
public class SystemUserTest {
    
    
    /**
     * Test of setMerchant method, of class SystemUser.
     */
    @Test
    public void testBeanPattern() {
        Merchant m = new Merchant();
        SystemUser instance = new SystemUser();
        instance.setCredentialsExpired(true);
        instance.setDisabled(true);
        instance.setExpired(true);
        instance.setId("id");
        instance.setLocked(true);
        instance.setMerchant(m);
        instance.setPassword("password");
        instance.setUsername("username");
        
        assertEquals(true, instance.isCredentialsExpired());
        assertEquals(true, instance.isDisabled());
        assertEquals(true, instance.isExpired());
        assertEquals("id", instance.getId());
        assertEquals(true, instance.isLocked());
        assertEquals(m, instance.getMerchant());
        assertEquals("password", instance.getPassword());
        assertEquals("username", instance.getUsername());
        
        
    }
}

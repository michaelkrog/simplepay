package dk.apaq.simplepay.model;

import dk.apaq.simplepay.gateway.PaymentGatewayType;
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
public class MerchantTest {
    
    @Test
    public void testBeanPattern() {
        Merchant instance = new Merchant();
        instance.setCity("city");
        instance.setCountry("country");
        instance.setEmail("email");
        instance.setGatewaySecret("gatewaySecret");
        instance.setGatewayType(PaymentGatewayType.Test);
        instance.setGatewayUserId("gatewayUserId");
        instance.setId("id");
        instance.setName("name");
        instance.setPhone("phone");
        instance.setRoad("road");
        instance.setZipcode("zip");
        
        assertEquals("city", instance.getCity());
        assertEquals("country", instance.getCountry());
        assertEquals("email", instance.getEmail());
        assertEquals("gatewaySecret", instance.getGatewaySecret());
        assertEquals(PaymentGatewayType.Test, instance.getGatewayType());
        assertEquals("gatewayUserId", instance.getGatewayUserId());
        assertEquals("id", instance.getId());
        assertEquals("name", instance.getName());
        assertEquals("phone", instance.getPhone());
        assertEquals("road", instance.getRoad());
        assertEquals("zip", instance.getZipcode());
        
    }

 
}

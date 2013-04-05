package dk.apaq.simplepay.model;

import dk.apaq.simplepay.gateway.EPaymentGateway;
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
        instance.getPaymentGatewayAccesses().add(new PaymentGatewayAccess(EPaymentGateway.Test, null));
       /* instance.setGatewaySecret("gatewaySecret");
        instance.setGatewayType(PaymentGatewayType.Test);
        instance.setGatewayUserId("gatewayUserId");*/
        instance.setId("id");
        instance.setName("name");
        instance.setPhone("phone");
        instance.setStreet("road");
        instance.setPostalCode("zip");
        
        assertEquals("city", instance.getCity());
        assertEquals("country", instance.getCountryCode());
        assertEquals("email", instance.getEmail());
        assertEquals(EPaymentGateway.Test, instance.getPaymentGatewayAccesses().get(0).getPaymentGatewayType());
        /*assertEquals("gatewaySecret", instance.getGatewaySecret());
        assertEquals(PaymentGatewayType.Test, instance.getGatewayType());
        assertEquals("gatewayUserId", instance.getGatewayUserId());*/
        assertEquals("id", instance.getId());
        assertEquals("name", instance.getName());
        assertEquals("phone", instance.getPhone());
        assertEquals("road", instance.getStreet());
        assertEquals("zip", instance.getPostalCode());
        
    }

 
}

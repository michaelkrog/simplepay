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
 * @author krog
 */
public class PaymentGatewayAccessTest {
    
    @Test
    public void testBeanPattern() {
        System.out.println("getPaymentGatewayType");
        PaymentGatewayAccess instance = new PaymentGatewayAccess();
        instance.setAcquirerRefId("refId");
        instance.setPaymentGatewayType(EPaymentGateway.Test);
        
        assertEquals("refId", instance.getAcquirerRefId());
        assertEquals(EPaymentGateway.Test, instance.getPaymentGatewayType());
    }

    
}

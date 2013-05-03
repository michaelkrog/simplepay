package dk.apaq.simplepay.data;

import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class MerchantRepositoryTest {
    
    @Autowired
    private IPayService service;
    
    /**
     * Test of createNew method, of class TokenCrud.
     */
    @Test
    public void testCreateNew() {
        System.out.println("createNew");
        
        Merchant m = new Merchant();
        m.setName("merchant");
        m.getPaymentGatewayAccesses().add(new PaymentGatewayAccess(EPaymentGateway.Test, "qwerty"));
        m = service.getMerchants().save(m);

        m = service.getMerchants().findOne(m.getId());
        assertEquals("merchant", m.getName());
        assertEquals(EPaymentGateway.Test, m.getPaymentGatewayAccesses().get(0).getPaymentGatewayType());
        assertEquals("qwerty", m.getPaymentGatewayAccesses().get(0).getAcquirerRefId());
        
    }

    

}

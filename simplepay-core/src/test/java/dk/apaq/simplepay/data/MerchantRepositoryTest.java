package dk.apaq.simplepay.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.data.ITokenRepository;
import dk.apaq.simplepay.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.ETokenPurpose;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class MerchantRepositoryTest {
    
    @Autowired
    private IPayService service;
    
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private StringEncryptor encryptor;
    

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

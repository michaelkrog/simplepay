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
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
//@Transactional
public class TokenRepositoryTest {
    
    @Autowired
    private IPayService service;
    
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private StringEncryptor encryptor;
    

    private Card card = new Card("xxxxxxxxxxx", 2012, 12, "xxx");
    
    /**
     * Test of createNew method, of class TokenCrud.
     */
    @Test
    public void testCreateNew() {
        System.out.println("createNew");
        
        Merchant m = new Merchant();
        m = service.getMerchants().save(m);
        
        EPaymentGateway gatewayType = EPaymentGateway.Test;
        String orderNumber = "ordernum";
        String description = "description";
        
        ITokenRepository rep = service.getTokens(m);
        Token token = rep.createNew(card);
        
        //The one we get back from the repository has been decrypted.
        assertEquals("xxxxxxxxxxx", token.getData().getCardNumber());
        assertEquals("xxx", token.getData().getCvd());
        
        
        token = em.find(Token.class, token.getId());
        
        //The one we read directly via the entitymanager is encrypted.
        assertNotNull(token);
        assertEquals(ETokenPurpose.SinglePayment, token.getPurpose());
        assertEquals("xxxxxxxxxxx", encryptor.decrypt(token.getData().getCardNumber()));
        assertEquals("xxx", encryptor.decrypt(token.getData().getCvd()));
        
        assertFalse(rep.findAll().isEmpty());
        
    }

    

}

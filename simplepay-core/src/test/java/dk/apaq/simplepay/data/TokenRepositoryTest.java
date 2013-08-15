package dk.apaq.simplepay.data;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.PaymentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.service.TokenService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
@Ignore
public class TokenRepositoryTest {
    
    @Autowired
    private PaymentContext context;
    
    @Autowired
    private StringEncryptor encryptor;
    

    @Before
    public void init() {
        this.card = new Card("4111111111111111", 2012, 12, "xxx", encryptor);
    }
    
    private Card card;
    
    /**
     * Test of createNew method, of class TokenCrud.
     */
    @Test
    public void testCreateNew() {
        System.out.println("createNew");
        
        Merchant m = new Merchant();
        m = context.getMerchantService().save(m);
        
        EPaymentGateway gatewayType = EPaymentGateway.Test;
        String orderNumber = "ordernum";
        String description = "description";
        
        TokenService service = context.getTokenService();
        Token token = service.createNew(m, card);
        
        //The one we get back from the repository has been decrypted.
        assertEquals("4111111111111111", token.getData().getCardNumber(encryptor));
        assertEquals("xxx", token.getData().getCvd(encryptor));
        
        
        /*token = em.find(Token.class, token.getId());
        
        //The one we read directly via the entitymanager is encrypted.
        assertNotNull(token);
        assertEquals(ETokenPurpose.SinglePayment, token.getPurpose());
        assertEquals("4111111111111111", encryptor.decrypt(token.getData().getCardNumber(encryptor)));
        assertEquals("xxx", encryptor.decrypt(token.getData().getCvd()));*/
        
        assertTrue(service.findAll().iterator().hasNext());
        
    }

    

}

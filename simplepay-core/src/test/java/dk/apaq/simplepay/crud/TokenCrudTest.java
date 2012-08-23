package dk.apaq.simplepay.crud;

import dk.apaq.simplepay.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Card;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.TokenPurpose;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
@Transactional
public class TokenCrudTest {
    
    @Autowired
    private IPayService service;
    

    private Card card = new Card("4571123412341234",12, 2012, "123");
    
    /**
     * Test of createNew method, of class TokenCrud.
     */
    @Test
    public void testCreateNew() {
        System.out.println("createNew");
        
        Merchant m = new Merchant();
        m = service.getMerchants().createAndRead(m);
        
        PaymentGatewayType gatewayType = PaymentGatewayType.Test;
        String orderNumber = "ordernum";
        String description = "description";
        
        ITokenCrud crud = service.getTokens(m);
        Token token = crud.createNew(card);
        assertNotNull(token);
        //assertEquals(0, token.getAuthorizedAmount());
        //assertEquals(false, token.isAuthorized());
        assertEquals(TokenPurpose.SinglePayment, token.getPurpose());
        
        assertFalse(crud.list().isEmpty());
        
    }

    

}

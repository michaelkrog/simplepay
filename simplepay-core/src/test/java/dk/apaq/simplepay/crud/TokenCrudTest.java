package dk.apaq.simplepay.crud;

import dk.apaq.simplepay.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import dk.apaq.simplepay.common.PaymentMethod;
import dk.apaq.simplepay.gateway.PaymentException;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Card;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.TokenPurpose;
import org.hibernate.validator.constraints.impl.LuhnValidator;
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

    /**
     * Test of authorizedRemote method, of class TokenCrud.
     */
    @Test
    public void testAuthorizedRemote() {
        System.out.println("authorizedRemote");
        
        Merchant m = new Merchant();
        m = service.getMerchants().createAndRead(m);
        ITokenCrud crud = service.getTokens(m);
        Token token = crud.createNew(card);
        
        String currency = "DKK";
        long amount = 1000L;
        PaymentMethod paymentMethod = PaymentMethod.Dankort;
        int expireMonth = 12;
        int expireYear = 12;
        String cardNumberTruncated = "xxxxxxxxxx";
        String remoteTransactionID = "123";
        //assertTrue(result.isAuthorized());
        
    }

    /**
     * Test of authorize method, of class TokenCrud.
     */
    @Test
    public void testAuthorize() {
        System.out.println("authorize");
        Merchant m = new Merchant();
        m = service.getMerchants().createAndRead(m);
        ITokenCrud crud = service.getTokens(m);
        Token token = crud.createNew(card);
        String currency = "DKK";
        long amount = 1000L;
        PaymentMethod method = PaymentMethod.Dankort;
        String cardNumber = "4571";
        String cvd = "123";
        int expireMonth = 12;
        int expireYear = 12;
        //assertTrue(result.isAuthorized());
    }
    
    @Test
    public void testAuthorizeNotMarkedOnError() throws InterruptedException {
        System.out.println("authorize");
        Merchant m = new Merchant();
        m = service.getMerchants().createAndRead(m);
        final ITokenCrud crud = service.getTokens(m);
        final Token token = crud.createNew(card);
        String currency = "DKK";
        long amount = 10000000L;
        PaymentMethod method = PaymentMethod.Dankort;
        String cardNumber = "4571";
        String cvd = "123";
        int expireMonth = 12;
        int expireYear = 12;
        
        try {
            fail("Should have failed because of too large amount.");
        } catch(PaymentException ex) {
            //Test gateway fails on large amounts
        }
        
        //Read from a new thread to makes sure that the data we read is not cached in relation to the thread.
        final Token[] result = new Token[1];
        
        Runnable r = new Runnable() {

            public void run() {
                result[0] = crud.read(token.getId());
            }
        };
        
        Thread t = new Thread(r);
        t.start();
        t.join();
        
        //assertFalse(result[0].isAuthorized());
    }
    

}

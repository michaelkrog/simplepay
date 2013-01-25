package dk.apaq.simplepay.api;

import java.util.List;

import dk.apaq.framework.common.beans.finance.PaymentIntrument;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.model.Token;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author krog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class TokenControllerTest {
    
    @Autowired
    private IPayService service;
    
    public TokenControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createToken method, of class TokenController.
     */
    @Test
    public void testCreateToken() {
        System.out.println("createToken");
        String cardNumber = "1234 5678 1234 5678";
        int expireMonth = 11;
        int expireYear = 2016;
        String cvd = "123";
        TokenController instance = new TokenController(service);
        String result = instance.createToken(cardNumber, expireMonth, expireYear, cvd);
        assertNotNull(result);
    }

    /**
     * Test of listTokens method, of class TokenController.
     */
    @Test
    public void testListTokens() {
        System.out.println("listTokens");
        TokenController instance = new TokenController(service);
        List<Token> result = instance.listTokens();
        assertNotNull(result);
    }

    /**
     * Test of getToken method, of class TokenController.
     */
    @Test
    public void testGetToken() {
        System.out.println("getToken");
        String cardNumber = "4485 5381 6916 0095";
        int expireMonth = 11;
        int expireYear = 2016;
        String cvd = "123";
        TokenController instance = new TokenController(service);
        String token = instance.createToken(cardNumber, expireMonth, expireYear, cvd);
        assertNotNull(token);
        
        Token tokenObj = instance.getToken(token);
        assertEquals("4485538169160095", tokenObj.getData().getCardNumber());
        assertTrue(tokenObj.getData().isValid());
        assertEquals(PaymentIntrument.Visa, tokenObj.getData().getResolvedInstrument());
        
    }
}

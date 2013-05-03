package dk.apaq.simplepay.controllers.api;

import java.util.ArrayList;
import java.util.List;

import dk.apaq.framework.common.beans.finance.PaymentInstrument;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.controllers.exceptions.ParameterException;
import dk.apaq.simplepay.controllers.exceptions.ResourceNotFoundException;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.security.ERole;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    
    
    @Before
    public void setUp() {
        ((StandardPBEStringEncryptor)encryptor).setPassword("qwerty");
        merchant = service.getMerchants().save(new Merchant());
        user = service.getUsers().save(new SystemUser(merchant, "john", "doe", ERole.Merchant));
        
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        authList.add(new SimpleGrantedAuthority("ROLE_" + ERole.Merchant.name().toUpperCase()));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authList));
    }
    
    @After
    public void tearDown() {
    }
    
    private SystemUser user;
    private Merchant merchant;
    private StringEncryptor encryptor = new StandardPBEStringEncryptor();

    /**
     * Test of createToken method, of class TokenController.
     */
    @Test
    public void testCreateToken() {
        System.out.println("createToken");
        String cardNumber = "4111 1111 1111 1111";
        int expireMonth = 11;
        int expireYear = 2016;
        String cvd = "123";
        TokenApiController instance = new TokenApiController(service, encryptor, null);
        Token result = instance.createToken(cardNumber, expireMonth, expireYear, cvd);
        assertNotNull(result);
    }

    /**
     * Test of listTokens method, of class TokenController.
     */
    @Test
    public void testListTokens() {
        System.out.println("listTokens");
        TokenApiController instance = new TokenApiController(service, encryptor, null);
        Iterable<Token> result = instance.listTokens();
        assertNotNull(result);
    }

    /**
     * Test of getToken method, of class TokenController.
     */
    @Test
    public void testGetToken() {
        System.out.println("getToken");
        String cardNumber = "4485538169160095";
        int expireMonth = 11;
        int expireYear = 2016;
        String cvd = "123";
        TokenApiController instance = new TokenApiController(service, encryptor, null);
        Token token = instance.createToken(cardNumber, expireMonth, expireYear, cvd);
        assertNotNull(token);
        
        Token tokenObj = token;
        assertEquals("4485538169160095", tokenObj.getData().getCardNumber(encryptor));
        assertEquals(2016, tokenObj.getData().getExpireYear());
        assertTrue(tokenObj.getData().isValid());
        assertEquals(PaymentInstrument.Visa, tokenObj.getData().getPaymentInstrument());
        
    }
    
        @Test
    public void testGetToken2() {
        System.out.println("getToken");
        String cardNumber = "4485 5381 6916 0095";
        int expireMonth = 11;
        int expireYear = 16;
        String cvd = "123";
        TokenApiController instance = new TokenApiController(service, encryptor, null);
        Token token = instance.createToken(cardNumber, expireMonth, expireYear, cvd);
        assertNotNull(token);
        
        Token tokenObj = token;
        assertEquals("4485538169160095", tokenObj.getData().getCardNumber(encryptor));
        assertEquals(2016, tokenObj.getData().getExpireYear());
        assertTrue(tokenObj.getData().isValid());
        assertEquals(PaymentInstrument.Visa, tokenObj.getData().getPaymentInstrument());
        
    }
        
    @Test
    public void testGetToken_InvalidCard() {
        System.out.println("getToken");
        String cardNumber = "345345435345";
        int expireMonth = 11;
        int expireYear = 16;
        String cvd = "123";
        TokenApiController instance = new TokenApiController(service, encryptor, null);
        
        try {
            Token token = instance.createToken(cardNumber, expireMonth, expireYear, cvd);
            fail("Should have failed.");
        } catch (ParameterException ex) {
            assertEquals("cardNumber", ex.getParameterName());
        }
        
    }
        
    @Test
    public void testGetToken_IllegalArguments() {
        System.out.println("getToken");
        String cardNumber = "ubuntu";
        int expireMonth = 43;
        int expireYear = 316;
        String cvd = "wwer";
        TokenApiController instance = new TokenApiController(service, encryptor, null);
        
        try {
            Token token = instance.createToken(cardNumber, expireMonth, expireYear, cvd);
            fail("Should have failed.");
        } catch(IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    @Test
    public void testGetToken_NotFound() {
        TokenApiController instance = new TokenApiController(service, encryptor, null);
        
        try {
            Token tokenObj = instance.getToken("234re");
            fail("Should have failed");
        } catch(ResourceNotFoundException ex) {
            
        }
    }
}

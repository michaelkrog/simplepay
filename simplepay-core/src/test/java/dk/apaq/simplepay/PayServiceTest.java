package dk.apaq.simplepay;

import dk.apaq.simplepay.common.PaymentMethod;
import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Role;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;
/**
 *
 * @author krog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
@Transactional
public class PayServiceTest {
    
    @Autowired
    private IPayService service;
    
    private void login(SystemUser user) {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        authList.add(new SimpleGrantedAuthority("ROLE_" + Role.Merchant.name().toUpperCase()));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authList));
    }
    
    /**
     * Test of setApplicationContext method, of class PayService.
     */
    @Test
    public void testWorkWithMerchant() {
        Merchant m = new Merchant();
        m = service.getMerchants().createAndRead(m);
        
        SystemUser user = service.getUsers().createAndRead(new SystemUser(m, "john", "doe"));
        
        Merchant m2 = new Merchant();
        m2 = service.getMerchants().createAndRead(m2);
        
        SystemUser user2 = service.getUsers().createAndRead(new SystemUser(m, "jane", "doe"));
        
        //We are not logged in - we should not be allowed to change this merchant.
        try {
            service.getMerchants().update(m);
            fail("Should not be able to update merchant.");
        } catch(Exception ex) { }
        
        login(user);
        
        //Now we should be allowed
        m = service.getMerchants().update(m);
        
        //But not for m2
        try {
            service.getMerchants().update(m2);
            fail("Should not be able to update merchant.");
        } catch(Exception ex) { }
        
        Token token1 = service.getTokens(m).createNew(PaymentGatewayType.QuickPay, "");
        Token token2 = service.getTokens(m2).createNew(PaymentGatewayType.QuickPay, "");
        
        //Create transaction for m
        Transaction t = service.getTransactions(m).createNew(token1, "T_123");
        
        //Create transaction for m2
        Transaction t2 = service.getTransactions(m2).createNew(token2, "T_321");
        
        //Make sure the right data has been set
        assertEquals(m.getId(), t.getMerchant().getId());
        assertEquals(TransactionStatus.Ready, t.getStatus());
        
        //Make sure that transactions are only available throught he right merchants
        List<Transaction> tlist = service.getTransactions(m).list();
        List<Transaction> tlist2 = service.getTransactions(m2).list();
        assertEquals(1, tlist.size());
        assertEquals(1, tlist2.size());
        assertEquals("T_123", tlist.get(0).getOrderNumber());
        assertEquals("T_321", tlist2.get(0).getOrderNumber());
        
    }
    
    @Test
    public void testValidPayment() {
        Merchant m = new Merchant();
        m = service.getMerchants().createAndRead(m);
        
        Token token = service.getTokens(m).createNew(PaymentGatewayType.QuickPay, "");
        assertFalse(token.isUsed());
        assertFalse(token.isAuthorized());
        
        token = service.getTokens(m).authorizedRemote(token, "DKK", 300, PaymentMethod.Visa, 8, 12, "XXXX XXXX XXXX 1234", null);
        assertFalse(token.isUsed());
        assertTrue(token.isAuthorized());
        
        //Create transaction for m
        Transaction t = service.getTransactions(m).createNew(token, "T_13123");
        assertEquals(t.getToken().getId(), token.getId());
        assertTrue(t.getToken().isAuthorized());
        assertTrue(t.getToken().isUsed());
        assertEquals(TransactionStatus.Ready, t.getStatus());
        
        t = service.getTransactions(m).charge(t, 300);
        assertEquals(TransactionStatus.Charged, t.getStatus());
        
        t = service.getTransactions(m).refund(t, 300);
        assertEquals(TransactionStatus.Refunded, t.getStatus());
        
    }
    
    @Test
    public void testInvalidPayment() {
        Merchant m = new Merchant();
        m = service.getMerchants().createAndRead(m);
        
        Token token = service.getTokens(m).createNew(PaymentGatewayType.QuickPay, "");
        assertFalse(token.isUsed());
        assertFalse(token.isAuthorized());
        
        token = service.getTokens(m).authorizedRemote(token, "DKK", 300, PaymentMethod.Visa, 8, 12, "XXXX XXXX XXXX 1234", null);
        assertFalse(token.isUsed());
        assertTrue(token.isAuthorized());
        
        //Create transaction for m
        Transaction t = service.getTransactions(m).createNew(token, "T_1312");
        
        try {
            service.getTransactions(m).createNew(token, "T_1234");
            fail("Should not allow same token twice.");
        } catch(SecurityException ex) { }
        
        //The orginal token is not uptodate and persisting it will change it to not used which is not allowed
        try {
            token = service.getTokens(m).authorizedRemote(token, "DKK", 300, PaymentMethod.Visa, 8, 12, "XXXX XXXX XXXX 1234", null);
            fail("Should not allow persisting token as not used");
        } catch(SecurityException ex) { }
    }
    
    
}

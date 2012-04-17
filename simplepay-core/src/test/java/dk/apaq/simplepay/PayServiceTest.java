package dk.apaq.simplepay;

import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Role;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionStatus;
import dk.apaq.simplepay.security.SystemUserDetails;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import static org.junit.Assert.*;
/**
 *
 * @author krog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class PayServiceTest {
    
    @Autowired
    private PayService service;
    
    private void login(SystemUser user) {
        SystemUserDetails mud = new SystemUserDetails(user);
        mud.getAuthorities().add(new SimpleGrantedAuthority("ROLE_" + Role.Merchant.name()));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(mud, null, mud.getAuthorities()));
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
        
        //Create transaction for m
        Transaction t = new Transaction();
        t.setOrderNumber("T_123");
        t.setGatewayType(PaymentGatewayType.QuickPay);
        t = service.getTransactions(m).createAndRead(t);
        
        //Create transaction for m2
        Transaction t2 = new Transaction();
        t2.setOrderNumber("T_321");
        t2.setGatewayType(PaymentGatewayType.QuickPay);
        t2 = service.getTransactions(m2).createAndRead(t2);
        
        //Make sure the right data has been set
        assertEquals(m.getId(), t.getMerchant().getId());
        assertEquals(TransactionStatus.Unauthorized, t.getStatus());
        
        //Make sure that transactions are only available throught he right merchants
        List<Transaction> tlist = service.getTransactions(m).list();
        List<Transaction> tlist2 = service.getTransactions(m2).list();
        assertEquals(1, tlist.size());
        assertEquals(1, tlist2.size());
        assertEquals("T_123", tlist.get(0).getOrderNumber());
        assertEquals("T_321", tlist2.get(0).getOrderNumber());
        
    }
}

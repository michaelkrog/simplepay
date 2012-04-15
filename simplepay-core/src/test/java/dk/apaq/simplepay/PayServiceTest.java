package dk.apaq.simplepay;

import dk.apaq.simplepay.gateway.PaymentGatewayType;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.model.TransactionStatus;
import dk.apaq.simplepay.security.MerchantUserDetails;
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
    
    private void login(Merchant m) {
        MerchantUserDetails mud = new MerchantUserDetails(m);
        mud.getAuthorities().add(new SimpleGrantedAuthority("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(mud, null, mud.getAuthorities()));
    }
    
    /**
     * Test of setApplicationContext method, of class PayService.
     */
    @Test
    public void testWorkWithMerchant() {
        Merchant m = new Merchant();
        m = service.getMerchants().createAndRead(m);
        
        //We are not logged in - we should not be allowed to change this merchant.
        try {
            service.getMerchants().update(m);
            fail("Should not be able to update merchant.");
        } catch(Exception ex) { }
        
        login(m);
        
        //Now we should be allowed
        m = service.getMerchants().update(m);
        
        Transaction t = new Transaction();
        t.setOrderNumber("T_123");
        t.setGatewayType(PaymentGatewayType.QuickPay);
        t = service.getTransactions(m).createAndRead(t);
        
        assertEquals(m.getId(), t.getMerchant().getId());
        assertEquals(TransactionStatus.Unauthorized, t.getStatus());
        
        
    }
}

package dk.apaq.simplepay;

import dk.apaq.framework.criteria.Criteria;
import dk.apaq.framework.criteria.Rules;
import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.model.*;
import dk.apaq.simplepay.security.ERole;
import java.util.ArrayList;
import java.util.List;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.framework.common.beans.finance.PaymentIntrument;
import dk.apaq.simplepay.gateway.PaymentException;
import org.jasypt.encryption.StringEncryptor;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
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
import org.junit.Before;
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
    
    @Autowired
    private StringEncryptor encryptor;
    
    @Before
    public void init() {
        dankort = new Card("4111111111111111",12, 12, "xxx", encryptor);
    }
    
    private Card dankort;
    
    private void login(SystemUser user) {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        authList.add(new SimpleGrantedAuthority("ROLE_" + ERole.Merchant.name().toUpperCase()));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authList));
    }
    
    /**
     * Test of setApplicationContext method, of class PayService.
     */
    @Test
    public void testWorkWithMerchant() {
        Merchant m = new Merchant();
        m.getPaymentGatewayAccesses().add(new PaymentGatewayAccess(EPaymentGateway.Test, null));
        
        m = service.getMerchants().save(m);
        
        SystemUser user = service.getUsers().save(new SystemUser(m, "john", "doe"));
        
        Merchant m2 = new Merchant();
        m2.getPaymentGatewayAccesses().add(new PaymentGatewayAccess(EPaymentGateway.Test, null));
        m2 = service.getMerchants().save(m2);
        
        SystemUser user2 = service.getUsers().save(new SystemUser(m, "jane", "doe"));
        
        //We are not logged in - we should not be allowed to change this merchant.
        try {
            service.getMerchants().save(m);
            fail("Should not be able to update merchant.");
        } catch(Exception ex) { }
        
        login(user);
        
        //Now we should be allowed
        m = service.getMerchants().save(m);
        
        //But not for m2
        try {
            service.getMerchants().save(m2);
            fail("Should not be able to update merchant.");
        } catch(Exception ex) { }
        
        Token token1 = service.getTokens(m).createNew(dankort);
        Token token2 = service.getTokens(m2).createNew(dankort);
        
        //Create transaction for m
        Transaction t = service.getTransactions(m).createNew(token1.getMerchant(), token1.getId(), "T_123", Money.of(CurrencyUnit.USD, 123));
        
        //Create transaction for m2
        Transaction t2 = service.getTransactions(m2).createNew(token2.getMerchant(), token2.getId(), "T_321", Money.of(CurrencyUnit.USD, 123));
        
        //Make sure the right data has been set
        assertEquals(m.getId(), t.getMerchant().getId());
        assertEquals(ETransactionStatus.Authorized, t.getStatus());
        
        //Make sure that transactions are only available throught he right merchants
        Iterable<Transaction> tlist = service.getTransactions(m).findAll();
        Iterable<Transaction> tlist2 = service.getTransactions(m2).findAll();
        assertTrue(tlist.iterator().hasNext());
        assertTrue(tlist2.iterator().hasNext());
        assertEquals("T_123", tlist.iterator().next().getRefId());
        assertEquals("T_321", tlist2.iterator().next().getRefId());
        
    }

    
    @Test
    public void testValidTestPayment() {
        Merchant m = new Merchant();
        m.getPaymentGatewayAccesses().add(new PaymentGatewayAccess(EPaymentGateway.Test, null));
        m = service.getMerchants().save(m);
        
        Token token = service.getTokens(m).createNew(dankort);
        assertFalse(token.isExpired());
        
        //Create transaction for m
        Transaction t = service.getTransactions(m).createNew(token.getMerchant(), token.getId(), "T_123", Money.of(CurrencyUnit.USD, 123));
        assertEquals(t.getToken(), token.getId());
        assertEquals(ETransactionStatus.Authorized, t.getStatus());
        
        t = service.getTransactions(m).charge(t, 300);
        assertEquals(ETransactionStatus.Charged, t.getStatus());
        
        t = service.getTransactions(m).refund(t, 300);
        assertEquals(ETransactionStatus.Refunded, t.getStatus());
        
    }
    
    @Test
    public void testInvalidTestPayment() {
        Merchant m = new Merchant();
        m.getPaymentGatewayAccesses().add(new PaymentGatewayAccess(EPaymentGateway.Test, null));
        m = service.getMerchants().save(m);
        
        Token token = service.getTokens(m).createNew(dankort);
        assertFalse(token.isExpired());
        
        //Create transaction for m
        Transaction t = service.getTransactions(m).createNew(token.getMerchant(), token.getId(), "T_123", Money.of(CurrencyUnit.USD, 123));
        
        try {
            service.getTransactions(m).createNew(token.getMerchant(), token.getId(), "T_321", Money.of(CurrencyUnit.USD, 123));
            fail("Should not allow same token twice.");
        } catch(IllegalArgumentException ex) { }
    }
    
    @Test
    public void testMissingGatewayAccess() {
        Merchant m = new Merchant();
        m.getPaymentGatewayAccesses().add(new PaymentGatewayAccess(EPaymentGateway.Test, null, PaymentIntrument.Mastercard));
        m = service.getMerchants().save(m);
        
        Token token = service.getTokens(m).createNew(dankort);
        assertFalse(token.isExpired());
        
        try {
            Transaction t = service.getTransactions(m).createNew(token.getMerchant(), token.getId(), "T_123", Money.of(CurrencyUnit.USD, 123));
            fail("Should have failed");
        } catch(PaymentException ex) {
            
        }
        
    }
    
    @Test
    public void testGetEvents() {
        Transaction t = new Transaction("123", "T_123", Money.of(CurrencyUnit.USD, 123), EPaymentGateway.Test);
        Merchant merchant = service.getMerchants().save(new Merchant());
        Repository<BaseEvent, String> events = service.getEvents(merchant, BaseEvent.class);
        TransactionEvent event = events.save(new TransactionEvent(t, "user", ETransactionStatus.Authorized, "129.129.129.912"));
        assertNotNull(event);
    }
    
    @Test
    public void testGetOrCreatePublicUser() {
        Merchant merchant = service.getMerchants().save(new Merchant());
        Iterable<SystemUser> users = service.getUsers().findAll(new Criteria(Rules.equals("merchant", merchant)));
        assertFalse(users.iterator().hasNext());
        
        SystemUser user = service.getOrCreatePublicUser(merchant);
        assertNotNull(user);
        
        users = service.getUsers().findAll(new Criteria(Rules.equals("merchant", merchant)));
        assertTrue(users.iterator().hasNext());
    }
    
    @Test
    public void testGetOrCreatePrivateUser() {
        Merchant merchant = service.getMerchants().save(new Merchant());
        Iterable<SystemUser> users = service.getUsers().findAll(new Criteria(Rules.equals("merchant", merchant)));
        assertFalse(users.iterator().hasNext());
        
        SystemUser user = service.getOrCreatePrivateUser(merchant);
        assertNotNull(user);
        
        users = service.getUsers().findAll(new Criteria(Rules.equals("merchant", merchant)));
        assertTrue(users.iterator().hasNext());
    }
    
    @Test
    public void testEvents() {
        Merchant merchant = service.getMerchants().save(new Merchant());
        Iterable<TokenEvent> evts = service.getEvents(merchant, TokenEvent.class).findAll();
        assertFalse(evts.iterator().hasNext());
        
        Token token = service.getTokens(merchant).createNew(dankort);
        
        evts = service.getEvents(merchant, TokenEvent.class).findAll();
        assertTrue(evts.iterator().hasNext());
        
    }
}

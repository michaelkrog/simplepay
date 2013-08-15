package dk.apaq.simplepay.service;

import java.util.HashMap;
import java.util.Map;
import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.gateway.IPaymentGateway;
import dk.apaq.simplepay.gateway.PaymentGatewayManager;
import dk.apaq.simplepay.gateway.test.TestGateway;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.repository.ISystemUserRepository;
import dk.apaq.simplepay.security.ERole;
import dk.apaq.simplepay.util.MockMerchantRepository;
import dk.apaq.simplepay.util.MockTokenRepository;
import dk.apaq.simplepay.util.MockTransactionRepository;
import dk.apaq.simplepay.util.MockUserRepository;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.joda.money.Money;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author krog
 */
public class TransactionServiceTest {
    
    private MockMerchantRepository merchantRepository = new MockMerchantRepository();
    private MockTokenRepository tokenRepository = new MockTokenRepository();
    private MerchantService merchantService = new MerchantService();
    private TokenService tokenService = new TokenService();
    private MockTransactionRepository transactionRepository = new MockTransactionRepository();
    private TransactionService transactionService = new TransactionService();
    private MockUserRepository userRepository = new MockUserRepository();
    private UserService userService = new UserService();
    
    private Merchant merchant;
    private SystemUser systemUser;
    
    @Before
    public void init() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("qwerty");
        card = new Card("4111111111111111", 12, 12, "xxx", encryptor);
        
        Map<String, IPaymentGateway> gwMap = new HashMap<String, IPaymentGateway>();
        gwMap.put(EPaymentGateway.Test.name(), new TestGateway());
        PaymentGatewayManager gatewayManager = new PaymentGatewayManager();
        gatewayManager.setGatewayMap(gwMap);
        
        userService.setRepository(userRepository);
        merchantService.setRepository(merchantRepository);
        tokenService.setRepository(tokenRepository);
        tokenService.setUserService(userService);
        transactionService.setRepository(transactionRepository);
        transactionService.setUserService(userService);
        transactionService.setTokenService(tokenService);
        transactionService.setGatewayManager(gatewayManager);
        
        merchant = new Merchant();
        merchant.getPaymentGatewayAccesses().add(new PaymentGatewayAccess(EPaymentGateway.Test, null));
        merchant = merchantService.save(merchant);
        
        systemUser = new SystemUser(merchant, "john", "doe", ERole.Merchant);
        systemUser = userService.save(systemUser);
        
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("john", "doe"));
        
    }
    
    private Card card;
    
    @Test
    public void testFullChargeAndRefund() {
        
        EPaymentGateway gatewayType = EPaymentGateway.Test;
        String orderNumber = "ordernum";
        String description = "description";
        Money money = Money.parse("USD 123.45");
        
        Token token = tokenService.createNew(merchant, card);
        
        Transaction t = transactionService.createNew(token.getId(), orderNumber, money);
        
        assertNotNull(t);
        assertEquals(12345, t.getAmount());
        assertEquals(0, t.getAmountCharged());
        assertEquals(0, t.getAmountRefunded());
        assertEquals("USD", t.getCurrency());
        assertEquals(ETransactionStatus.Authorized, t.getStatus());
        
        t = transactionService.charge(t, t.getAmount());
        assertEquals(12345, t.getAmount());
        assertEquals(12345, t.getAmountCharged());
        assertEquals(0, t.getAmountRefunded());
        assertEquals(ETransactionStatus.Charged, t.getStatus());
        
        t = transactionService.refund(t, t.getAmount());
        assertEquals(12345, t.getAmount());
        assertEquals(12345, t.getAmountCharged());
        assertEquals(12345, t.getAmountRefunded());
        assertEquals(ETransactionStatus.Refunded, t.getStatus());
        
    }

    @Test
    public void testCancel() {
        System.out.println("createNew");
        
        EPaymentGateway gatewayType = EPaymentGateway.Test;
        String orderNumber = "o_" + System.currentTimeMillis();
        String description = "description";
        Money money = Money.parse("USD 123.45");
        
        Token token = tokenService.createNew(merchant, card);
        
        Transaction t = transactionService.createNew(token.getId(), orderNumber, money);
        
        assertNotNull(t);
        assertEquals(12345, t.getAmount());
        assertEquals(0, t.getAmountCharged());
        assertEquals(0, t.getAmountRefunded());
        assertEquals("USD", t.getCurrency());
        assertEquals(ETransactionStatus.Authorized, t.getStatus());
        
        t = transactionService.cancel(t);
        assertEquals(12345, t.getAmount());
        assertEquals(0, t.getAmountCharged());
        assertEquals(0, t.getAmountRefunded());
        assertEquals(ETransactionStatus.Cancelled, t.getStatus());

        
    }
}
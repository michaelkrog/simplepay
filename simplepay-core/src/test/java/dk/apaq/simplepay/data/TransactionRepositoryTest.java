package dk.apaq.simplepay.data;

import dk.apaq.simplepay.repository.ITransactionRepository;
import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.PaymentContext;
import dk.apaq.simplepay.common.ETransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.service.TokenService;
import dk.apaq.simplepay.service.TransactionService;
import org.jasypt.encryption.StringEncryptor;
import org.joda.money.Money;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author michael
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
public class TransactionRepositoryTest {
    
    @Autowired
    private PaymentContext context;
    
    @Autowired
    private StringEncryptor encryptor;
    

    @Before
    public void init() {
        card = new Card("4111111111111111", 12, 12, "xxx", encryptor);
    }
    
    private Card card;
    
    @Test
    public void testFullChargeAndRefund() {
        System.out.println("createNew");
        
        Merchant m = new Merchant();
        m.getPaymentGatewayAccesses().add(new PaymentGatewayAccess(EPaymentGateway.Test, null));
        m = context.getMerchantService().save(m);
        
        EPaymentGateway gatewayType = EPaymentGateway.Test;
        String orderNumber = "ordernum";
        String description = "description";
        Money money = Money.parse("USD 123.45");
        
        TokenService tokens = context.getTokenService();
        Token token = tokens.createNew(card);
        
        TransactionService transactions = context.getTransactionService();
        Transaction t = transactions.createNew(token.getId(), orderNumber, money);
        
        assertNotNull(t);
        assertEquals(12345, t.getAmount());
        assertEquals(0, t.getAmountCharged());
        assertEquals(0, t.getAmountRefunded());
        assertEquals("USD", t.getCurrency());
        assertEquals(ETransactionStatus.Authorized, t.getStatus());
        
        t = transactions.charge(t, t.getAmount());
        assertEquals(12345, t.getAmount());
        assertEquals(12345, t.getAmountCharged());
        assertEquals(0, t.getAmountRefunded());
        assertEquals(ETransactionStatus.Charged, t.getStatus());
        
        t = transactions.refund(t, t.getAmount());
        assertEquals(12345, t.getAmount());
        assertEquals(12345, t.getAmountCharged());
        assertEquals(12345, t.getAmountRefunded());
        assertEquals(ETransactionStatus.Refunded, t.getStatus());
        
    }

    @Test
    public void testCancel() {
        System.out.println("createNew");
        
        Merchant m = new Merchant();
        m.getPaymentGatewayAccesses().add(new PaymentGatewayAccess(EPaymentGateway.Test, null));
        m = context.getMerchantService().save(m);
        
        EPaymentGateway gatewayType = EPaymentGateway.Test;
        String orderNumber = "o_" + System.currentTimeMillis();
        String description = "description";
        Money money = Money.parse("USD 123.45");
        
        TokenService tokens = context.getTokenService();
        Token token = tokens.createNew(card);
        
        TransactionService transactions = context.getTransactionService();
        Transaction t = transactions.createNew(token.getId(), orderNumber, money);
        
        assertNotNull(t);
        assertEquals(12345, t.getAmount());
        assertEquals(0, t.getAmountCharged());
        assertEquals(0, t.getAmountRefunded());
        assertEquals("USD", t.getCurrency());
        assertEquals(ETransactionStatus.Authorized, t.getStatus());
        
        t = transactions.cancel(t);
        assertEquals(12345, t.getAmount());
        assertEquals(0, t.getAmountCharged());
        assertEquals(0, t.getAmountRefunded());
        assertEquals(ETransactionStatus.Cancelled, t.getStatus());

        
    }
    

}

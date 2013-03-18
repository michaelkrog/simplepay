package dk.apaq.simplepay.controllers.api;

import java.util.List;
import java.util.UUID;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.framework.repository.CollectionRepository;
import dk.apaq.framework.repository.Repository;
import dk.apaq.simplepay.IPayService;
import dk.apaq.simplepay.common.ETransactionStatus;
import dk.apaq.simplepay.controllers.exceptions.ResourceNotFoundException;
import dk.apaq.simplepay.data.ITokenRepository;
import dk.apaq.simplepay.data.ITransactionRepository;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.SystemUser;
import dk.apaq.simplepay.model.Token;
import dk.apaq.simplepay.model.Transaction;
import dk.apaq.simplepay.security.ERole;
import org.apache.commons.lang.Validate;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class TransactionControllerTest {

    private class MockTokenRepository extends CollectionRepository<Token> implements ITokenRepository {

        public MockTokenRepository() {
            super(new IdResolver<Token>() {
                @Override
                public String getIdForBean(Token bean) {
                    return bean.getId();
                }
            });
        }

        @Override
        public Token createNew(Card card) {
            return save(new Token(card));
        }

        @Override
        public Token markExpired(String token) {
            Token t = findOne(token);
            t.setExpired(true);
            return save(t);
        }

        @Override
        public Token save(Token entity) {
            entity.setId(UUID.randomUUID().toString());
            return super.save(entity);
        }
    }

    private class MockTransactionRepository extends CollectionRepository<Transaction> implements ITransactionRepository {

        public MockTransactionRepository() {
            super(new IdResolver<Transaction>() {
                @Override
                public String getIdForBean(Transaction bean) {
                    return bean.getId();
                }
            });
        }

        @Override
        public Transaction createNew(Merchant merchant, String tokenId, String refId, Money money) {
            Validate.notNull(tokenRep.findOne(tokenId), "token not found");
            return save(new Transaction(tokenId, refId, money, EPaymentGateway.Test));
        }

        @Override
        public Transaction charge(Transaction transaction, long amount) {
            transaction.setAmountCharged(amount);
            transaction.setStatus(ETransactionStatus.Charged);
            return save(transaction);
        }

        @Override
        public Transaction cancel(Transaction transaction) {
            transaction.setStatus(ETransactionStatus.Cancelled);
            return save(transaction);
        }

        @Override
        public Transaction refund(Transaction transaction, long amount) {
            transaction.setAmountRefunded(amount);
            transaction.setStatus(ETransactionStatus.Refunded);
            return save(transaction);
        }

        @Override
        public Transaction save(Transaction entity) {
            entity.setId(UUID.randomUUID().toString());
            return super.save(entity);
        }
        
        
    }

    @Before
    public void init() {
        ((StandardPBEStringEncryptor)encryptor).setPassword("qwerty");
        card = new Card("1234123412341234", 2016, 11, "123", encryptor);
        merchant = new Merchant();
        Token token = tokenRep.createNew(card);
        
        for (int i = 0; i < 20; i++) {
            transactionRep.createNew(merchant, token.getId(), "#" + i, Money.parse("USD 123.00"));
        }
        
        
        //Prepare for security check
        SystemUser user = userRep.save(new SystemUser(merchant, "john", "doe", ERole.Merchant));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("john", "doe"));
        
        service = Mockito.mock(IPayService.class);
        Mockito.when(service.getUser(Mockito.anyString())).thenReturn(user);
        
        Mockito.when(service.getTokens(merchant)).thenReturn(tokenRep);
        Mockito.when(service.getTransactions(merchant)).thenReturn(transactionRep);
        Mockito.when(service.getUsers()).thenReturn(userRep);
        
        
    }
    
    private Merchant merchant;
    private Repository<SystemUser, String> userRep = new CollectionRepository<SystemUser>();
    private ITokenRepository tokenRep = new MockTokenRepository();
    private ITransactionRepository transactionRep = new MockTransactionRepository();
    private IPayService service;
    private StringEncryptor encryptor = new StandardPBEStringEncryptor();
    private Card card;

    /**
     * Test of listTransactionsAsJson method, of class TransactionController.
     */
    @Test
    public void testListTransactionsAsJson() {
        System.out.println("listTransactionsAsJson");
        String query = "refId like '#1*'";
        Integer offset = 0;
        Integer limit = 5;
        TransactionApiController instance = new TransactionApiController(service);
        List<Transaction> result = instance.listTransactions(query, offset, limit);

        assertTrue(result.size() == 5);
        assertTrue(result.get(0).getRefId().startsWith("#1"));
    }

    /**
     * Test of createTransaction method, of class TransactionController.
     */
    @Test
    public void testCreateTransaction() {
        System.out.println("createTransaction");

        String refId = "refid";
        String currency = "DKK";
        Integer amount = 10000;
        String token = service.getTokens(merchant).createNew(card).getId();
        TransactionApiController instance = new TransactionApiController(service);
        
        Transaction result = instance.createTransaction(token, refId, currency, amount);

        assertNotNull(result);
        
        Transaction t = result;
        assertNotNull(t);
        assertEquals(token, t.getToken());
        assertEquals("refid", t.getRefId());
        assertEquals("DKK", t.getCurrency());
        assertEquals(10000, t.getAmount());
    }
    
    @Test
    public void testCreateTransactionInvalidToken() {
        System.out.println("createTransaction");

        String refId = "refid";
        String currency = "DKK";
        Integer amount = 10000;
        String token = "blabla";
        TransactionApiController instance = new TransactionApiController(service);
        
        try {
            Transaction result = instance.createTransaction(token, refId, currency, amount);
            fail("Should have failed");
        } catch(IllegalArgumentException ex) {
            
        }
        
    }

    /**
     * Test of getTransaction method, of class TransactionController.
     */
    @Test
    @Ignore
    public void testGetTransaction() {
        System.out.println("getTransaction");
        String id = "";
        TransactionApiController instance = new TransactionApiController(service);
        Transaction expResult = null;
        Transaction result = instance.getTransaction(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refundTransaction method, of class TransactionController.
     */
    @Test
    @Ignore
    public void testRefundTransaction() {
        System.out.println("refundTransaction");
        String id = "";
        Long amount = null;
        TransactionApiController instance = new TransactionApiController(service);
        Transaction expResult = null;
        Transaction result = instance.refundTransaction(id, amount);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of chargeTransaction method, of class TransactionController.
     */
    @Test
    @Ignore
    public void testChargeTransaction() {
        System.out.println("chargeTransaction");
        String id = "";
        Long amount = null;
        TransactionApiController instance = new TransactionApiController(service);
        Transaction expResult = null;
        Transaction result = instance.chargeTransaction(id, amount);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cancelTransaction method, of class TransactionController.
     */
    @Test
    @Ignore
    public void testCancelTransaction() {
        System.out.println("cancelTransaction");
        String id = "";
        TransactionApiController instance = new TransactionApiController(service);
        Transaction expResult = null;
        Transaction result = instance.cancelTransaction(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


}

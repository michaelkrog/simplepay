package dk.apaq.simplepay.gateway.nets;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.nets.payment.Nets;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import dk.apaq.simplepay.model.ETokenPurpose;
import dk.apaq.simplepay.model.Merchant;
import dk.apaq.simplepay.model.PaymentGatewayAccess;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.joda.money.Money;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

/**
 *
 * @author krog
 */
public class NetsGatewayTest {
    
    
    @Before
    public void setUp() {
        nets = Mockito.mock(Nets.class);
        
        merchant = new Merchant();
        merchant.setName("merchant");
        
        access = new PaymentGatewayAccess(EPaymentGateway.Nets, "refId");
        
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("qwerty");
        
        card = new Card("4571111111111111", 2016, 12, "123", encryptor);
        
        gateway = new NetsGateway(nets);
    }
    
    private Nets nets;
    private NetsGateway gateway;
    private Merchant merchant;
    private PaymentGatewayAccess access;
    private StandardPBEStringEncryptor encryptor;
    private Card card;
    
    /**
     * Test of authorize method, of class NetsGateway.
     */
    @Test
    public void testAuthorize() {
        System.out.println("authorize");
        Money money = Money.parse("DKK 212");
        String orderId = "orderid";
        String terminalId = "termid";
        ETokenPurpose purpose = ETokenPurpose.RecurringPayment;
        gateway.authorize(merchant, access, card, money, orderId, terminalId, purpose);
        
    }

    /**
     * Test of cancel method, of class NetsGateway.
     */
    @Test
    public void testCancel() {
        System.out.println("cancel");
        Merchant sMerchant = null;
        PaymentGatewayAccess access = null;
        String transactionId = "";
        String orderId = "";
        NetsGateway instance = null;
        instance.cancel(sMerchant, access, transactionId, orderId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of capture method, of class NetsGateway.
     */
    @Test
    public void testCapture() {
        System.out.println("capture");
        Merchant sMerchant = null;
        PaymentGatewayAccess access = null;
        String transactionId = "";
        String orderId = "";
        long amountInCents = 0L;
        NetsGateway instance = null;
        instance.capture(sMerchant, access, transactionId, orderId, amountInCents);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refund method, of class NetsGateway.
     */
    @Test
    public void testRefund() {
        System.out.println("refund");
        Merchant sMerchant = null;
        PaymentGatewayAccess access = null;
        String transactionId = "";
        String orderId = "";
        long amountInCents = 0L;
        NetsGateway instance = null;
        instance.refund(sMerchant, access, transactionId, orderId, amountInCents);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of renew method, of class NetsGateway.
     */
    @Test
    public void testRenew() {
        System.out.println("renew");
        Merchant sMerchant = null;
        PaymentGatewayAccess access = null;
        String transactionId = "";
        String orderId = "";
        long amountInCents = 0L;
        NetsGateway instance = null;
        instance.renew(sMerchant, access, transactionId, orderId, amountInCents);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
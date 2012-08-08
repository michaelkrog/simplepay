package dk.apaq.simplepay.model;

import dk.apaq.simplepay.common.TransactionStatus;
import dk.apaq.simplepay.gateway.PaymentGatewayType;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
public class TransactionTest {
    
    @Test
    public void testBeanPattern() {
        Date now = new Date();
        Merchant m = new Merchant();
        /*Token t = new Token(PaymentGatewayType.Test, "ordernum", "description");
        Transaction instance = new Transaction();
        instance.setCapturedAmount(100);
        instance.setCurrency("DKK");
        instance.setDateChanged(now);
        instance.setDateCreated(now);
        instance.setDescription("description");
        instance.setId("id");
        instance.setMerchant(m);
        instance.setOrderNumber("ordernum");
        instance.setRefundedAmount(100);
        instance.setStatus(TransactionStatus.Refunded);
        instance.setToken(t);
        
        assertEquals(100, instance.getCapturedAmount());
        assertEquals("DKK", instance.getCurrency());
        assertEquals(now, instance.getDateChanged());
        assertEquals(now, instance.getDateChanged());
        assertEquals("description", instance.getDescription());
        assertEquals("id", instance.getId());
        assertEquals(m, instance.getMerchant());
        assertEquals("ordernum", instance.getOrderNumber());
        assertEquals(100, instance.getRefundedAmount());
        assertEquals(TransactionStatus.Refunded, instance.getStatus());
        assertEquals(t, instance.getToken());*/
        
        
    }
}

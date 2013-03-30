package dk.apaq.simplepay.model;


import dk.apaq.simplepay.gateway.EPaymentGateway;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
public class TokenTest {
    
    @Test
    public void testBeanPattern() {
        Date now = new Date();
        Merchant m = new Merchant();
        
        /*Token instance = new Token(PaymentGatewayType.Test, "ordernum", null);
        instance.setAuthorized(true);
        instance.setAuthorizedAmount(100);
        instance.setCardExpireMonth(8);
        instance.setCardExpireYear(2012);
        
        instance.setCardNumber("cardNumber");
        instance.setCardNumberLast4("cardNumberTruncated");
        instance.setPaymentMethod(PaymentMethod.Dankort);
        instance.setCurrency("currency");
        instance.setDateChanged(now);
        instance.setDateCreated(now);
        instance.setGatewayTransactionId("gatewayId");
        instance.setId("id");
        instance.setMerchant(m);
        instance.setPurpose(TokenPurpose.SinglePayment);
        instance.setUsed(true);
        
        assertTrue(instance.isAuthorized());
        assertEquals(100, instance.getAuthorizedAmount());
        assertEquals(8, instance.getCardExpireMonth());
        assertEquals(2012, instance.getCardExpireYear());
        assertEquals("cardNumber", instance.getCardNumber());
        assertEquals("cardNumberTruncated", instance.getCardNumberLast4());
        assertEquals(PaymentMethod.Dankort, instance.getPaymentMethod());
        assertEquals("currency", instance.getCurrency());
        assertEquals(now, instance.getDateChanged());
        assertEquals(now, instance.getDateCreated());
        assertEquals("gatewayId", instance.getGatewayTransactionId());
        assertEquals(PaymentGatewayType.Test, instance.getGatewayType());
        assertEquals("id", instance.getId());
        assertEquals(m, instance.getMerchant());
        assertEquals(TokenPurpose.SinglePayment, instance.getPurpose());
        assertTrue(instance.isUsed());
        assertEquals("ordernum", instance.getOrderNumber());
        assertEquals("", instance.getDescription());*/
    }

}

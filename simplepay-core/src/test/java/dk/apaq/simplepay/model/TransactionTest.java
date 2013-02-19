package dk.apaq.simplepay.model;

import dk.apaq.simplepay.common.ETransactionStatus;
import java.util.Date;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.simplepay.gateway.EPaymentGateway;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.joda.money.Money;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;

/**
 *
 * @author michael
 */
public class TransactionTest {
    
    @Test
    public void testBeanPattern() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("qwerty");
        
        Date now = new Date();
        Merchant m = new Merchant();
        Card card = new Card("1234567812345678", 2016, 11, "123", encryptor);
        Money money = Money.parse("USD 100");
        
        Transaction instance = new Transaction("token", "refId", money, EPaymentGateway.Nets);
        instance.setAmountCharged(10000);
        instance.setAmountRefunded(10000);
        instance.setDateChanged(now);
        instance.setDateCreated(now);
        instance.setDescription("description");
        instance.setGatewayTransactionId("gti");
        instance.setId("id");
        instance.setMerchant(m);
        instance.setMessage("message");
        instance.setStatus(ETransactionStatus.Refunded);
        instance.setTest(true);
        instance.setToken("token");
        
        assertEquals(10000, instance.getAmount());
        assertEquals(10000, instance.getAmountCharged());
        assertEquals(10000, instance.getAmountRefunded());
        assertEquals("USD", instance.getCurrency());
        assertEquals(now, instance.getDateChanged());
        assertEquals(now, instance.getDateChanged());
        assertEquals("description", instance.getDescription());
        assertEquals("gti", instance.getGatewayTransactionId());
        assertEquals(EPaymentGateway.Nets, instance.getGatewayType());
        assertEquals("id", instance.getId());
        assertEquals(m, instance.getMerchant());
        assertEquals("message", instance.getMessage());
        assertEquals("refId", instance.getRefId());
        assertEquals(ETransactionStatus.Refunded, instance.getStatus());
        assertEquals(true, instance.isTest());
        assertEquals("token", instance.getToken());
        
        
    }
}

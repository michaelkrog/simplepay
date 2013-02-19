package dk.apaq.simplepay.model;

import dk.apaq.framework.common.beans.finance.Card;
import dk.apaq.nets.payment.ActionCode;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.joda.money.Money;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class NetsTransactionDataTest {

    @Test
    public void testBeanPattern() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("qwerty");
        
        Card card = new Card("1234567812345678", 2012, 11, "123", encryptor);
        Money money = Money.parse("USD 123.45");
        
        NetsTransactionData instance = new NetsTransactionData();
        instance.setActionCode(ActionCode.Approved);
        instance.setApprovalCode("approvalCode");
        instance.setApprovedAmount(money);
        instance.setCard(card);
        instance.setId("id");
        instance.setOde("ode");
        instance.setProcessingCode("processingCode");
        
        assertEquals(ActionCode.Approved, instance.getActionCode());
        assertEquals("approvalCode", instance.getApprovalCode());
        assertEquals(money, instance.getApprovedAmount());
        assertEquals(card, instance.getCard());
        assertEquals("id", instance.getId());
        assertEquals("ode", instance.getOde());
        assertEquals("processingCode", instance.getProcessingCode());
             
    }

    
}


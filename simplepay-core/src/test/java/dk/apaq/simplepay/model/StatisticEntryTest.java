package dk.apaq.simplepay.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class StatisticEntryTest {

    @Test
    public void testBeanPattern() {
        System.out.println("getMerchantId");
        StatisticEntry instance = new StatisticEntry("merchantId", 12, 99, 1, 2, 3, 4, 5, 6, 7, 8);
        assertEquals("merchantId", instance.getMerchantId());
        assertEquals(12, instance.getHour());
        assertEquals(99, instance.getAmount());
        assertEquals(1, instance.getAmericanExpressCount());
        assertEquals(2, instance.getDankortCount());
        assertEquals(3, instance.getDinersCount());
        assertEquals(4, instance.getJcbCount());
        assertEquals(5, instance.getMastercardCount());
        assertEquals(6, instance.getVisaCount());
        assertEquals(7, instance.getVisaElectronCount());
        assertEquals(8, instance.getUnknownCount());

    }
}

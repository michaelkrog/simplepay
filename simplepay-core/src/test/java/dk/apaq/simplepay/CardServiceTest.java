package dk.apaq.simplepay;

import dk.apaq.simplepay.model.Card;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;
/**
 *
 * @author krog
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/defaultspringcontext.xml"})
@Transactional
public class CardServiceTest {
    
    @Autowired
    private CardService cardService;
    
    
    /**
     * Test of setApplicationContext method, of class PayService.
     */
    @Test
    public void testGenerateCard() {
        Card card = cardService.generateCard("Michael Krog", "4571123412341234", 2, 2014, "123");
        assertEquals("Michael Krog", card.getName());
        assertEquals("1234", card.getLast4());
        assertEquals(2, card.getExpMonth());
        assertEquals(2014, card.getExpYear());
        assertEquals("4571123412341234", cardService.decrypt(card.getEncryptedNumber()));
        assertEquals("123", cardService.decrypt(card.getEncryptedCvd()));
    }
}

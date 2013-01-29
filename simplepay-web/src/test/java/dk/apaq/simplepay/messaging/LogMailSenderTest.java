package dk.apaq.simplepay.messaging;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.mail.SimpleMailMessage;

/**
 *
 * @author krog
 */
public class LogMailSenderTest {
    /**
     * Test of send method, of class LogMailSender.
     */
    @Test
    public void testSend_SimpleMailMessage() {
        System.out.println("send");
        SimpleMailMessage msg = new SimpleMailMessage();
        
        LogMailSender instance = new LogMailSender();
        instance.send(msg);
        
    }

    /**
     * Test of send method, of class LogMailSender.
     */
    @Test
    public void testSend_SimpleMailMessageArr() {
        System.out.println("send");
        SimpleMailMessage[] simpleMessages = new SimpleMailMessage[1];
        simpleMessages[0] = new SimpleMailMessage();
        LogMailSender instance = new LogMailSender();
        instance.send(simpleMessages);
        
    }
}

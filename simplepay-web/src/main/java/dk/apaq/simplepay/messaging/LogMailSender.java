package dk.apaq.simplepay.messaging;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * A simple mailsender for logging the mails send. Practical for development purposes.
 *
 * @author krog
 */
public class LogMailSender implements MailSender {

    private static final Logger LOG = LoggerFactory.getLogger(LogMailSender.class);


    @Override
    public void send(SimpleMailMessage msg) throws MailException {
                try {
                    String to = Arrays.toString(msg.getTo());
                    String from = msg.getFrom();
                    String text = msg.getText();
                    LOG.info("Mail send -------------------------\nRecipient: {}\nSender: {}\nText: {}\n-------------------",
                            new Object[]{to, from, text});
                } catch (Exception ex) {
                    LOG.error("Unable to send mail.", ex);
                }
            
    }

    @Override
    public void send(SimpleMailMessage[] simpleMessages) throws MailException {
        for(SimpleMailMessage smm : simpleMessages) {
            send(smm);
        }
    }
    
    
}

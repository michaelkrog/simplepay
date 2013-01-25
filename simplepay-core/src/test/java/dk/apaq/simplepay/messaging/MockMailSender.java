package dk.apaq.simplepay.messaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 *
 * @author michael
 */
public class MockMailSender implements MailSender {

    boolean mailSend;
    private List<SimpleMailMessage> messages = new ArrayList<SimpleMailMessage>();

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        mailSend = true;
        messages.add(simpleMessage);
    }

    @Override
    public void send(SimpleMailMessage[] simpleMessages) throws MailException {
        mailSend = true;
        messages.addAll(Arrays.asList(simpleMessages));
    }

    public boolean isMailSend() {
        return mailSend;
    }

    public void reset() {
        mailSend = false;
        messages.clear();
    }

    public List<SimpleMailMessage> getMessages() {
        return messages;
    }

    public SimpleMailMessage lastMessageSent() {
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }
}

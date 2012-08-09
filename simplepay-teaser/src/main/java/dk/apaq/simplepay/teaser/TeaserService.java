package dk.apaq.simplepay.teaser;

import dk.apaq.crud.Crud;
import dk.apaq.simplepay.teaser.model.NotificationReceiver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author krog
 */
public class TeaserService implements ITeaserService {

    @Autowired
    private Crud.Complete<String, NotificationReceiver> notificationRecievers;

    @Override
    public Crud.Complete<String, NotificationReceiver> getNotificationRecievers() {
        return notificationRecievers;
    }
    
}

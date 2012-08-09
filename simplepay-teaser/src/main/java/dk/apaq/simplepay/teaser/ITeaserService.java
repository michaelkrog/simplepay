package dk.apaq.simplepay.teaser;

import dk.apaq.crud.Crud;
import dk.apaq.simplepay.teaser.model.NotificationReceiver;

/**
 *
 * @author krog
 */
public interface ITeaserService {

    Crud.Complete<String, NotificationReceiver> getNotificationRecievers();
    
}

package dk.apaq.simplepay.data.service;

import dk.apaq.simplepay.model.BaseEvent;

/**
 *
 * @author krog
 */
public interface IEventService {
    
    Iterable<BaseEvent> findAll();
    void save(BaseEvent event);
}

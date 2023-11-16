package dev.nos.djnavigator.event;

import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    private final EventManager eventManager;

    EventPublisher(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void publishEvent(Event event) {
        eventManager.notify(event);
    }
}

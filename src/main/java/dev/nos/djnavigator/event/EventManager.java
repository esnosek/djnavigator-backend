package dev.nos.djnavigator.event;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

@Slf4j
@SuppressWarnings({"rawtypes", "unchecked"})
class EventManager {

    private final static HashMap<EventType, HashSet<EventListener>> EVENT_LISTENERS = new HashMap<>();

    void subscribe(EventType type, EventListener listener) {
        var listeners = EVENT_LISTENERS.getOrDefault(type, new HashSet<>());
        listeners.add(listener);
        EVENT_LISTENERS.put(type, listeners);

        log.info(format(
                "Listener: %s has been subscribed to the event %s",
                listener.getClass().getName(),
                type
        ));
    }

    void unsubscribe(EventType type, EventListener listener) {
        ofNullable(EVENT_LISTENERS.get(type))
                .map(listeners -> listeners.remove(listener));
        log.info(format(
                "Listener: %s has been unsubscribed from the event %s",
                listener.getClass().getName(),
                type
        ));
    }

    void notify(Event event) {
        EVENT_LISTENERS
                .getOrDefault(event.type(), new HashSet<>())
                .stream()
                .peek(listener -> listener.onEvent(event))
                .forEach(listener ->
                        log.info(format(
                                "Listener: %s has been notified about the event %s",
                                listener.getClass().getName(),
                                event.type()
                        ))
                );
    }

}

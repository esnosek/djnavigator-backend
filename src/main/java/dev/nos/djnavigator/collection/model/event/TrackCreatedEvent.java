package dev.nos.djnavigator.collection.model.event;

import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.event.Event;
import dev.nos.djnavigator.event.EventType;

import static dev.nos.djnavigator.event.EventType.TRACK_ADDED_EVENT;

public record TrackCreatedEvent(Track track) implements Event {

    @Override
    public EventType type() {
        return TRACK_ADDED_EVENT;
    }
}

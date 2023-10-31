package dev.nos.djnavigator.collection.model.id;

import jakarta.persistence.Embeddable;

@Embeddable
public class TrackId extends EntityId {

    private TrackId() {
        super();
    }

    private TrackId(String value) {
        super(value);
    }

    public static TrackId from(String value) {
        return new TrackId(value);
    }

    public static TrackId randomId() {
        return new TrackId();
    }
}

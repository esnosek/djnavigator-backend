package dev.nos.djnavigator.collection.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class AlbumId extends EntityId {

    private AlbumId() {
        super();
    }

    private AlbumId(String value) {
        super(value);
    }

    public static AlbumId from(String value) {
        return new AlbumId(value);
    }

    public static AlbumId randomId() {
        return new AlbumId();
    }
}

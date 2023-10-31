package dev.nos.djnavigator.spotify.model.id;

import org.unbrokendome.base62.Base62;

import java.util.Objects;

import static java.util.UUID.randomUUID;


public abstract class SpotifyId {

    private final String idValue;

    protected SpotifyId(String value) {
        this.idValue = value;
    }

    protected SpotifyId() {
        this(Base62.encodeUUID(randomUUID()));
    }

    public String id() {
        return idValue;
    }

    @Override
    public String toString() {
        return id();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotifyId entityId = (SpotifyId) o;
        return Objects.equals(idValue, entityId.idValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idValue);
    }
}

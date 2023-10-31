package dev.nos.djnavigator.collection.model.id;

import jakarta.persistence.MappedSuperclass;
import org.unbrokendome.base62.Base62;

import java.util.Objects;

import static java.util.UUID.randomUUID;

@MappedSuperclass
public abstract class EntityId {

    private final String idValue;

    protected EntityId(String value) {
        this.idValue = value;
    }

    protected EntityId() {
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
        EntityId entityId = (EntityId) o;
        return Objects.equals(idValue, entityId.idValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idValue);
    }
}

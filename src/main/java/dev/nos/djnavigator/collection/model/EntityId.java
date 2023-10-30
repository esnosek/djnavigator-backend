package dev.nos.djnavigator.collection.model;

import jakarta.persistence.MappedSuperclass;
import org.unbrokendome.base62.Base62;

import static java.util.UUID.randomUUID;

@MappedSuperclass
public abstract class EntityId {

    private final String idValue;

    protected EntityId(String value) {
        this.idValue = value;
    }

    public EntityId() {
        this(Base62.encodeUUID(randomUUID()));
    }

    public String id() {
        return idValue;
    }
}

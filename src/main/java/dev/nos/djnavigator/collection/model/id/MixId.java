package dev.nos.djnavigator.collection.model.id;

import jakarta.persistence.Embeddable;

@Embeddable
public class MixId extends EntityId {

    private MixId() {
        super();
    }

    private MixId(String value) {
        super(value);
    }

    public static MixId from(String value) {
        return new MixId(value);
    }

    public static MixId randomId() {
        return new MixId();
    }
}

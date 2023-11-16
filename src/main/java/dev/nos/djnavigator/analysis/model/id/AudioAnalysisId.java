package dev.nos.djnavigator.analysis.model.id;

import dev.nos.djnavigator.collection.model.id.EntityId;
import jakarta.persistence.Embeddable;

@Embeddable
public class AudioAnalysisId extends EntityId {

    private AudioAnalysisId() {
        super();
    }

    private AudioAnalysisId(String value) {
        super(value);
    }

    public static AudioAnalysisId from(String value) {
        return new AudioAnalysisId(value);
    }

    public static AudioAnalysisId randomId() {
        return new AudioAnalysisId();
    }
}


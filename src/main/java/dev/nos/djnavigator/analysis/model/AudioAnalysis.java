package dev.nos.djnavigator.analysis.model;

import dev.nos.djnavigator.analysis.model.id.AudioAnalysisId;
import dev.nos.djnavigator.collection.model.Track;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AudioAnalysis {

    @EmbeddedId
    @AttributeOverride(name = "idValue", column = @Column(name = "id"))
    @Builder.Default
    private AudioAnalysisId id = AudioAnalysisId.randomId();

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @OneToOne(fetch = FetchType.LAZY)
    private Track track;

    private String data;
}

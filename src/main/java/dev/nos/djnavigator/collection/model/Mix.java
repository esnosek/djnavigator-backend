package dev.nos.djnavigator.collection.model;

import dev.nos.djnavigator.collection.model.id.MixId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverrides({
        @AttributeOverride(
                name = "leftTurntable.pitch",
                column = @Column(name = "left_turntable_pitch")
        ),
        @AttributeOverride(
                name = "rightTurntable.pitch",
                column = @Column(name = "right_turntable_pitch")
        )
})
@AssociationOverrides({
        @AssociationOverride(
                name = "leftTurntable.track",
                joinColumns = @JoinColumn(name = "left_turntable_track_id")
        ),
        @AssociationOverride(
                name = "rightTurntable.track",
                joinColumns = @JoinColumn(name = "right_turntable_track_id")
        )
})
public class Mix {

    @EmbeddedId
    @AttributeOverride(name = "idValue", column = @Column(name = "id"))
    @Builder.Default
    private MixId id = MixId.randomId();

    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime createdDate = now().truncatedTo(MILLIS);

    private Turntable leftTurntable;
    private Turntable rightTurntable;
}

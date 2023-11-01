package dev.nos.djnavigator.collection.model;

import dev.nos.djnavigator.collection.model.id.MixId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    private LocalDateTime createdDate;

    private Turntable leftTurntable;
    private Turntable rightTurntable;
}

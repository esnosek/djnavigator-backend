package dev.nos.djnavigator.collection.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;
    private Turntable leftTurntable;
    private Turntable rightTurntable;
}

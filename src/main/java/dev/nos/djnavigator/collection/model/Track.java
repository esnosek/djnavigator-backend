package dev.nos.djnavigator.collection.model;

import dev.nos.djnavigator.collection.model.converter.StringListConverter;
import dev.nos.djnavigator.collection.model.id.TrackId;
import dev.nos.djnavigator.collection.model.id.TrackSpotifyId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Track {

    @EmbeddedId
    @AttributeOverride(name = "idValue", column = @Column(name = "id"))
    @Builder.Default
    private TrackId id = TrackId.randomId();

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    private String name;

    @Convert(converter = StringListConverter.class)
    private List<String> artists;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "album_id")
    private Album album;

    private BigDecimal tempo;

    @Embedded
    @AttributeOverride(name = "idValue", column = @Column(name = "spotify_id"))
    private TrackSpotifyId spotifyId;
}

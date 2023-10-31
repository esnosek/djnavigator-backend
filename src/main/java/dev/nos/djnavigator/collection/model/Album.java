package dev.nos.djnavigator.collection.model;

import dev.nos.djnavigator.collection.model.converter.StringListConverter;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MILLIS;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @EmbeddedId
    @AttributeOverride(name = "idValue", column = @Column(name = "id"))
    @Builder.Default
    private AlbumId id = AlbumId.randomId();

    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime createdDate = now().truncatedTo(MILLIS);

    private String name;

    @Convert(converter = StringListConverter.class)
    private List<String> artists;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Track> tracks = new ArrayList<>();

    @Embedded
    @AttributeOverride(name = "idValue", column = @Column(name = "spotify_id"))
    private AlbumSpotifyId spotifyId;

    @Column(name = "image_path")
    private String imagePath;

    public void addTrack(Track track) {
        final var tracksBySpotifyId = this.tracks
                .stream()
                .map(Track::getSpotifyId)
                .toList();
        if (!tracksBySpotifyId.contains(track.getSpotifyId()))
            this.tracks.add(track);
    }

    public void addTracks(Collection<Track> tracks) {
        tracks.forEach(this::addTrack);
    }

}

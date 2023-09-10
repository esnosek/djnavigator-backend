package dev.nos.djnavigator.model;

import dev.nos.djnavigator.model.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;
    private String name;
    @Convert(converter = StringListConverter.class)
    private List<String> artists;
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<Track> tracks;
    @Column(name = "spotify_id")
    private String spotifyId;
    @Column(name = "image_path")
    private String imagePath;

    public Album withTracks(List<Track> tracks) {
        this.tracks = tracks;
        return this;
    }

    public Track getTrack(String trackId) {
        return this.tracks.stream()
                .filter(track -> Objects.equals(track.getId(), trackId))
                .findAny()
                .orElseThrow();
    }

    public void addTrack(Track track) {
        if (null == tracks) {
            tracks = new ArrayList<>();
        }
        final var tracksBySpotifyId = this.tracks.stream()
                .map(Track::getSpotifyId)
                .toList();
        if (!tracksBySpotifyId.contains(track.getSpotifyId()))
            this.tracks.add(track);
    }
}

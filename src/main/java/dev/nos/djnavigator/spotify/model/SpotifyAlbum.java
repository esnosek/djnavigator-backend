package dev.nos.djnavigator.spotify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpotifyAlbum(
        SpotifyAlbumId spotifyId,
        String name,
        List<String> artists,
        List<SpotifyTrack> spotifyTracks,
        String imagePath
) {
    public SpotifyAlbum withSpotifyTracks(List<SpotifyTrack> spotifyTracks) {
        return copy()
                .spotifyTracks(spotifyTracks)
                .build();
    }

    private SpotifyAlbum.SpotifyAlbumBuilder copy() {
        return SpotifyAlbum.builder()
                .spotifyId(this.spotifyId)
                .name(this.name)
                .artists(this.artists)
                .spotifyTracks(this.spotifyTracks)
                .imagePath(this.imagePath);
    }

    @JsonProperty("spotifyId")
    public String spotifyAlbumId() {
        return spotifyId != null ? spotifyId.id() : null;
    }
}

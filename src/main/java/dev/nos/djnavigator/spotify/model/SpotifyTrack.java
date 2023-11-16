package dev.nos.djnavigator.spotify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpotifyTrack(
        SpotifyTrackId spotifyId,
        String name,
        List<String> artists,
        SpotifyTrackAudioFeatures audioFeatures,
        SpotifyAlbum spotifyAlbum,
        String imagePath
) {

    public SpotifyAlbumId spotifyAlbumId() {
        return spotifyAlbum.spotifyId();
    }

    public SpotifyTrack withAudioFeatures(SpotifyTrackAudioFeatures audioFeatures) {
        return copy()
                .audioFeatures(audioFeatures)
                .build();
    }

    private SpotifyTrack.SpotifyTrackBuilder copy() {
        return SpotifyTrack.builder()
                .spotifyId(this.spotifyId)
                .name(this.name)
                .artists(this.artists)
                .audioFeatures(this.audioFeatures)
                .spotifyAlbum(this.spotifyAlbum)
                .imagePath(this.imagePath);
    }

    @JsonProperty("spotifyId")
    public String spotifyTrackId() {
        return spotifyId != null ? spotifyId.id() : null;
    }
}

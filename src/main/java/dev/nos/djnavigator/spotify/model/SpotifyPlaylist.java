package dev.nos.djnavigator.spotify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nos.djnavigator.spotify.model.id.SpotifyPlaylistId;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpotifyPlaylist(
        SpotifyPlaylistId spotifyId,
        List<SpotifyTrack> tracks
) {
    @JsonProperty("spotifyId")
    public String getSpotifyPlaylistId() {
        return spotifyId != null ? spotifyId.id() : null;
    }
}
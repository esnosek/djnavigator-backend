package dev.nos.djnavigator.spotify.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Jacksonized
public record SpotifySearchResults(
        @JsonProperty("tracks") List<SpotifyTrack> tracks,
        @JsonProperty("albums") List<SpotifyAlbum> albums) {
}

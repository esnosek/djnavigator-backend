package dev.nos.djnavigator.spotify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpotifySearchResults(
        @JsonProperty("tracks") List<SpotifyTrack> tracks,
        @JsonProperty("albums") List<SpotifyAlbum> albums) {
}

package dev.nos.djnavigator.spotify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpotifyAudioAnalysis(
        SpotifyTrackId id,
        JsonNode audioAnalysis
) {
    @JsonProperty("id")
    public String spotifyTrackId() {
        return id != null ? id.id() : null;
    }
}


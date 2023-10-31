package dev.nos.djnavigator.spotify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpotifyTrackAudioFeatures(
        SpotifyTrackId id,
        BigDecimal tempo
) {
    @JsonProperty("id")
    public String getSpotifyTrackId() {
        return id != null ? id.id() : null;
    }
}

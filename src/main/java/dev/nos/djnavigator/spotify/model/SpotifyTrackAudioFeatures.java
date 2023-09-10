package dev.nos.djnavigator.spotify.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Builder
@Jacksonized
public record SpotifyTrackAudioFeatures(
        String id,
        BigDecimal tempo
) {
}

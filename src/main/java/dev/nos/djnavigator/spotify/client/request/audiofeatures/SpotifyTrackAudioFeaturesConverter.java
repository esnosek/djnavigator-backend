package dev.nos.djnavigator.spotify.client.request.audiofeatures;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.model.SpotifyTrackAudioFeatures;

import java.math.BigDecimal;
import java.util.function.Function;

import static java.math.RoundingMode.HALF_UP;

public class SpotifyTrackAudioFeaturesConverter implements Function<JsonNode, SpotifyTrackAudioFeatures> {

    @Override
    public SpotifyTrackAudioFeatures apply(JsonNode audioFeatures) {
        return SpotifyTrackAudioFeatures.builder()
                .id(audioFeatures.get("id").asText())
                .tempo(roundTempo(audioFeatures))
                .build();
    }

    private static BigDecimal roundTempo(JsonNode audioFeatures) {
        return new BigDecimal(audioFeatures.get("tempo").asText())
                .setScale(2, HALF_UP);
    }
}

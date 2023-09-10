package dev.nos.djnavigator.spotify.converters;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.model.SpotifyTrackAudioFeatures;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.function.Function;

@Service
public class SpotifyTrackAudioFeaturesConverter implements Function<JsonNode, Map.Entry<String, SpotifyTrackAudioFeatures>> {

    @Override
    public Map.Entry<String, SpotifyTrackAudioFeatures> apply(JsonNode trackAudioFeatures) {
        final var audioFeature = SpotifyTrackAudioFeatures.builder()
                .tempo(new BigDecimal(trackAudioFeatures.get("tempo").asText())
                        .setScale(2, RoundingMode.HALF_UP)
                )
                .build();
        return new SimpleEntry<>(trackAudioFeatures.get("id").asText(), audioFeature);
    }
}

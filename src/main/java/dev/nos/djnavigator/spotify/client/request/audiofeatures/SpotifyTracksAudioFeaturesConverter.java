package dev.nos.djnavigator.spotify.client.request.audiofeatures;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.nos.djnavigator.spotify.model.SpotifyTrackAudioFeatures;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

public class SpotifyTracksAudioFeaturesConverter implements Function<JsonNode, Map<String, SpotifyTrackAudioFeatures>> {
    @Override
    public Map<String, SpotifyTrackAudioFeatures> apply(JsonNode audioFeatures) {
        return streamOf((ArrayNode) audioFeatures.get("audio_features"))
                .map(new SpotifyTrackAudioFeaturesConverter())
                .collect(toMap(
                        SpotifyTrackAudioFeatures::id,
                        identity()
                ));
    }

    private static Stream<JsonNode> streamOf(ArrayNode arrayNode) {
        return stream(arrayNode.spliterator(), false);
    }
}

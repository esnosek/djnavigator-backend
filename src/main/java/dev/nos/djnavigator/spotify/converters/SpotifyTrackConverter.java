package dev.nos.djnavigator.spotify.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Service
public class SpotifyTrackConverter implements Function<JsonNode, SpotifyTrack> {

    @Override
    public SpotifyTrack apply(JsonNode track) {
        return SpotifyTrack.builder()
                .spotifyId(track.get("id").asText())
                .name(track.get("name").asText())
                .artists(artists(track))
                .imagePath(
                        track.has("album")
                                ? track.get("album").get("images").get(0).get("url").asText()
                                : ""
                )
                .build();
    }

    private List<String> artists(JsonNode album) {
        final var artists = (ArrayNode) album.get("artists");
        return stream(artists.spliterator(), false)
                .map(artist -> artist.get("name").asText())
                .collect(toList());
    }
}

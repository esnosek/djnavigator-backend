package dev.nos.djnavigator.spotify.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.nos.djnavigator.spotify.model.SpotifyPlaylist;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Service
public class SpotifyPlaylistConverter implements Function<JsonNode, SpotifyPlaylist> {

    private final SpotifyTrackConverter spotifyTrackConverter;

    @Autowired
    public SpotifyPlaylistConverter(SpotifyTrackConverter spotifyTrackConverter) {
        this.spotifyTrackConverter = spotifyTrackConverter;
    }

    @Override
    public SpotifyPlaylist apply(JsonNode playlist) {
        return SpotifyPlaylist.builder()
                .id(playlist.get("id").asText())
                .tracks(toSpotifyTracks(playlist.get("tracks")))
                .build();
    }

    private List<SpotifyTrack> toSpotifyTracks(JsonNode tracks) {
        final var arrayNode = (ArrayNode) tracks.get("items");
        return stream(arrayNode.spliterator(), false)
                .map(item -> item.get("track"))
                .map(spotifyTrackConverter)
                .collect(toList());
    }

}

package dev.nos.djnavigator.spotify.client.request.playlist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.nos.djnavigator.spotify.client.request.track.SpotifyTrackConverter;
import dev.nos.djnavigator.spotify.model.SpotifyPlaylist;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.spotify.model.id.SpotifyPlaylistId;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.StreamSupport.stream;

public class SpotifyPlaylistConverter implements Function<JsonNode, SpotifyPlaylist> {

    @Override
    public SpotifyPlaylist apply(JsonNode playlist) {
        return SpotifyPlaylist.builder()
                .spotifyId(SpotifyPlaylistId.from(playlist.get("id").asText()))
                .tracks(spotifyTracks(playlist.get("tracks")))
                .build();
    }

    private static List<SpotifyTrack> spotifyTracks(JsonNode tracks) {
        final var arrayNode = (ArrayNode) tracks.get("items");
        return stream(arrayNode.spliterator(), false)
                .map(item -> item.get("track"))
                .map(new SpotifyTrackConverter())
                .toList();
    }

}

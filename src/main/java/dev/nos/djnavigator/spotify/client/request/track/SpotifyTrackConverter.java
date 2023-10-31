package dev.nos.djnavigator.spotify.client.request.track;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.nos.djnavigator.spotify.client.request.album.SpotifyAlbumConverter;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

public class SpotifyTrackConverter implements Function<JsonNode, SpotifyTrack> {

    @Override
    public SpotifyTrack apply(JsonNode track) {
        return SpotifyTrack.builder()
                .spotifyId(SpotifyTrackId.from(track.get("id").asText()))
                .name(track.get("name").asText())
                .artists(artists(track))
                .imagePath(imagePathIfProvided(track))
                .spotifyAlbum(albumIfProvided(track))
                .build();
    }

    private static SpotifyAlbum albumIfProvided(JsonNode track) {
        return track.has("album")
                ? new SpotifyAlbumConverter().apply(track.get("album"))
                : null;
    }

    private static String imagePathIfProvided(JsonNode track) {
        return track.has("album")
                ? track.get("album").get("images").get(0).get("url").asText()
                : null;
    }

    private static List<String> artists(JsonNode album) {
        return streamOf((ArrayNode) album.get("artists"))
                .map(artist -> artist.get("name").asText())
                .toList();
    }

    private static Stream<JsonNode> streamOf(ArrayNode arrayNode) {
        return stream(arrayNode.spliterator(), false);
    }
}

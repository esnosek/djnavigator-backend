package dev.nos.djnavigator.spotify.client.request.album;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.nos.djnavigator.spotify.client.request.track.SpotifyTrackConverter;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

public class SpotifyAlbumConverter implements Function<JsonNode, SpotifyAlbum> {

    @Override
    public SpotifyAlbum apply(JsonNode album) {
        return SpotifyAlbum.builder()
                .spotifyId(album.get("id").asText())
                .name(album.get("name").asText())
                .artists(artists(album))
                .imagePath(album.get("images").get(0).get("url").asText())
                .spotifyTracks(tracksIfProvided(album))
                .build();
    }

    private static List<SpotifyTrack> tracksIfProvided(JsonNode album) {
        return album.has("tracks")
                ? tracks((ArrayNode) album.get("tracks").get("items"))
                : null;
    }

    private static List<SpotifyTrack> tracks(ArrayNode tracks) {
        return streamOf(tracks)
                .map(new SpotifyTrackConverter())
                .toList();
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

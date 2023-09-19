package dev.nos.djnavigator.spotify.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.StreamSupport.stream;

@Service
public class SpotifyAlbumConverter implements Function<JsonNode, SpotifyAlbum> {

    @Override
    public SpotifyAlbum apply(JsonNode album) {
        return SpotifyAlbum.builder()
                .spotifyId(album.get("id").asText())
                .name(album.get("name").asText())
                .artists(artists(album))
                .imagePath(album.get("images").get(0).get("url").asText())
                .build();
    }


    private List<String> artists(JsonNode album) {
        final var artists = (ArrayNode) album.get("artists");
        return stream(artists.spliterator(), false)
                .map(artist -> artist.get("name").asText())
                .toList();
    }
}

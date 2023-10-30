package dev.nos.djnavigator.spotify.client.request.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.nos.djnavigator.spotify.client.request.album.SpotifyAlbumConverter;
import dev.nos.djnavigator.spotify.client.request.track.SpotifyTrackConverter;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifySearchResults;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;

import java.util.List;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class SpotifySearchResultsConverter implements Function<JsonNode, SpotifySearchResults> {

    @Override
    public SpotifySearchResults apply(JsonNode searchResults) {
        return SpotifySearchResults.builder()
                .albums(toSpotifyAlbums(searchResults.get("albums")))
                .tracks(toSpotifyTracks(searchResults.get("tracks")))
                .build();
    }

    private List<SpotifyAlbum> toSpotifyAlbums(JsonNode albums) {
        final var arrayNode = (ArrayNode) albums.get("items");
        return StreamSupport.stream(arrayNode.spliterator(), false)
                .map(new SpotifyAlbumConverter())
                .toList();
    }

    private List<SpotifyTrack> toSpotifyTracks(JsonNode tracks) {
        final var arrayNode = (ArrayNode) tracks.get("items");
        return StreamSupport.stream(arrayNode.spliterator(), false)
                .map(new SpotifyTrackConverter())
                .toList();
    }

}

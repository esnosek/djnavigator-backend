package dev.nos.djnavigator.spotify.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifySearchResults;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class SpotifySearchResultsConverter implements Function<JsonNode, SpotifySearchResults> {

    private final SpotifyAlbumConverter spotifyAlbumConverter;
    private final SpotifyTrackConverter spotifyTrackConverter;

    @Autowired
    public SpotifySearchResultsConverter(SpotifyAlbumConverter spotifyAlbumConverter, SpotifyTrackConverter spotifyTrackConverter) {
        this.spotifyAlbumConverter = spotifyAlbumConverter;
        this.spotifyTrackConverter = spotifyTrackConverter;
    }

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
                .map(spotifyAlbumConverter)
                .collect(toList());
    }

    private List<SpotifyTrack> toSpotifyTracks(JsonNode tracks) {
        final var arrayNode = (ArrayNode) tracks.get("items");
        return StreamSupport.stream(arrayNode.spliterator(), false)
                .map(spotifyTrackConverter)
                .collect(toList());
    }

}

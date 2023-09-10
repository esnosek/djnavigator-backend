package dev.nos.djnavigator.spotify.client;

import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.nos.djnavigator.spotify.converters.*;
import dev.nos.djnavigator.spotify.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

@Service
public class SpotifyQueries {

    private final SpotifyClient spotifyClient;
    private final SpotifySearchResultsConverter spotifySearchResultsConverter;
    private final SpotifyAlbumConverter spotifyAlbumConverter;
    private final SpotifyTrackConverter spotifyTrackConverter;
    private final SpotifyTrackAudioFeaturesConverter spotifyTrackAudioFeaturesConverter;
    private final SpotifyPlaylistConverter spotifyPlaylistConverter;

    @Autowired
    public SpotifyQueries(SpotifyClient spotifyClient,
                          SpotifySearchResultsConverter spotifySearchResultsConverter,
                          SpotifyAlbumConverter spotifyAlbumConverter,
                          SpotifyTrackConverter spotifyTrackConverter,
                          SpotifyTrackAudioFeaturesConverter spotifyTrackAudioFeaturesConverter,
                          SpotifyPlaylistConverter spotifyPlaylistConverter) {
        this.spotifyClient = spotifyClient;
        this.spotifySearchResultsConverter = spotifySearchResultsConverter;
        this.spotifyAlbumConverter = spotifyAlbumConverter;
        this.spotifyTrackConverter = spotifyTrackConverter;
        this.spotifyTrackAudioFeaturesConverter = spotifyTrackAudioFeaturesConverter;
        this.spotifyPlaylistConverter = spotifyPlaylistConverter;
    }

    public SpotifySearchResults searchAlbumOrTrack(String query, int limit) {
        final var uri = URI.create(format("https://api.spotify.com/v1/search?q=%s&type=%s&limit=%s", encode(query, UTF_8), "album,track", limit));
        return spotifySearchResultsConverter.apply(spotifyClient.saveGet(uri));
    }

    public SpotifyAlbum getAlbumWithTracks(String albumId) {
        final var uri = URI.create(format("https://api.spotify.com/v1/albums/%s", albumId));
        final var response = spotifyClient.saveGet(uri);
        final var spotifyTracks = stream(response.get("tracks").get("items").spliterator(), false)
                .map(spotifyTrackConverter)
                .toList();
        return spotifyAlbumConverter.apply(response)
                .withSpotifyTracks(spotifyTracks);
    }

    public SpotifyTrack getTrackWithAlbum(String trackId) {
        final var uri = URI.create(format("https://api.spotify.com/v1/tracks/%s", trackId));
        final var response = spotifyClient.saveGet(uri);
        final var album = spotifyAlbumConverter.apply(response.get("album"));
        return spotifyTrackConverter.apply(response)
                .withSpotifyAlbum(album);
    }

    public SpotifyPlaylist getPlaylist(String playlistId) {
        final var uri = URI.create(format("https://api.spotify.com/v1/playlists/%s", playlistId));
        return spotifyPlaylistConverter.apply(spotifyClient.saveGet(uri));
    }

    public SpotifyTrackAudioFeatures getAudioFeatureFor(String trackId) {
        final var uri = URI.create(format("https://api.spotify.com/v1/audio-features/%s", trackId));
        return spotifyTrackAudioFeaturesConverter.apply(spotifyClient.saveGet(uri)).getValue();
    }


    public Map<String, SpotifyTrackAudioFeatures> getAudioFeaturesFor(List<String> tracksIds) {
        final var uri = URI.create(format("https://api.spotify.com/v1/audio-features?ids=%s", String.join(",", tracksIds)));
        final var arrayNode = (ArrayNode) spotifyClient.saveGet(uri).get("audio_features");
        return stream(arrayNode.spliterator(), false)
                .map(spotifyTrackAudioFeaturesConverter)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}


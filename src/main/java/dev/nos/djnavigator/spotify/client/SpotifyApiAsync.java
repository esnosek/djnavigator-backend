package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static dev.nos.djnavigator.spotify.client.request.SpotifyThreading.executeAsync;

@Service
class SpotifyApiAsync {

    private final SpotifyApi spotifyApi;

    @Autowired
    SpotifyApiAsync(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    CompletableFuture<SpotifySearchResults> searchAlbumOrTrack(String query, int limit) {
        return executeAsync((() -> spotifyApi.searchAlbumOrTrack(query, limit)));
    }

    CompletableFuture<SpotifyAlbum> album(String albumId) {
        return executeAsync((() -> spotifyApi.album(albumId)));
    }

    CompletableFuture<SpotifyTrack> track(String trackId) {
        return executeAsync((() -> spotifyApi.track(trackId)));
    }

    CompletableFuture<SpotifyPlaylist> playlist(String playlistId) {
        return executeAsync((() -> spotifyApi.playlist(playlistId)));
    }

    CompletableFuture<SpotifyTrackAudioFeatures> audioFeatures(String trackId) {
        return executeAsync((() -> spotifyApi.audioFeatures(trackId)));
    }

    CompletableFuture<Map<String, SpotifyTrackAudioFeatures>> audioFeatures(List<String> tracksIds) {
        return executeAsync((() -> spotifyApi.audioFeatures(tracksIds)));
    }
}

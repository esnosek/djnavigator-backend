package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.model.*;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import dev.nos.djnavigator.spotify.model.id.SpotifyPlaylistId;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static dev.nos.djnavigator.spotify.config.SpotifyThreading.executeAsync;

@Service
public class SpotifyQueriesAsync {

    private final SpotifyApi spotifyApi;

    @Autowired
    SpotifyQueriesAsync(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    public CompletableFuture<SpotifyTrack> trackWithAudioFeature(SpotifyTrackId spotifyTrackId) {
        final var spotifyTrack = executeAsync(() ->
                spotifyApi.track(spotifyTrackId)
        );
        final var audioFeatures = executeAsync(() ->
                spotifyApi.audioFeatures(spotifyTrackId)
        );
        return spotifyTrack
                .thenCombine(
                        audioFeatures,
                        SpotifyTrack::withAudioFeatures
                );
    }

    public CompletableFuture<List<SpotifyTrack>> tracksWithAudioFeatures(SpotifyAlbumId spotifyAlbumId) {
        final var spotifyAlbum = executeAsync(() ->
                spotifyApi.album(spotifyAlbumId)
        );
        return spotifyAlbum
                .thenCompose(album ->
                        withAudioFeatures(album.spotifyTracks())
                );
    }

    public CompletableFuture<SpotifyAlbum> albumWithTracksAndAudioFeatures(SpotifyAlbumId spotifyAlbumId) {
        final var spotifyAlbum = executeAsync(() ->
                spotifyApi.album(spotifyAlbumId)
        );
        return spotifyAlbum
                .thenCompose(album ->
                        withAudioFeatures(album.spotifyTracks())
                )
                .thenCombine(
                        spotifyAlbum,
                        (tracks, album) -> album.withSpotifyTracks(tracks)
                );
    }

    public CompletableFuture<SpotifySearchResults> searchAlbumOrTrack(String query, int limit) {
        return executeAsync(() ->
                spotifyApi.searchAlbumOrTrack(query, limit)
        );
    }

    public CompletableFuture<SpotifyAlbum> albumWithTracks(SpotifyAlbumId albumId) {
        return executeAsync(() ->
                spotifyApi.album(albumId)
        );
    }

    public CompletableFuture<SpotifyPlaylist> playlist(SpotifyPlaylistId playlistId) {
        return executeAsync(() ->
                spotifyApi.playlist(playlistId)
        );
    }

    public CompletableFuture<SpotifyAudioAnalysis> audioAnalysis(SpotifyTrackId spotifyTrackId) {
        return executeAsync(() ->
                spotifyApi.audioAnalysis(spotifyTrackId)
        );
    }

    private CompletableFuture<List<SpotifyTrack>> withAudioFeatures(List<SpotifyTrack> spotifyTracks) {
        final var tracksAudioFeatures = executeAsync(() ->
                spotifyApi.audioFeatures(spotifyIds(spotifyTracks))
        );
        return tracksAudioFeatures
                .thenApply(audioFeatures ->
                        spotifyTracks
                                .stream()
                                .map(track -> withAudioFeature(track, audioFeatures))
                                .toList()
                );
    }

    private static SpotifyTrack withAudioFeature(SpotifyTrack track, Map<SpotifyTrackId, SpotifyTrackAudioFeatures> audioFeatures) {
        return track.withAudioFeatures(audioFeatures.get(track.spotifyId()));
    }

    private static List<SpotifyTrackId> spotifyIds(List<SpotifyTrack> spotifyTracks) {
        return spotifyTracks
                .stream()
                .map(SpotifyTrack::spotifyId)
                .toList();
    }

}

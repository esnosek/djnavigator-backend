package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifyPlaylist;
import dev.nos.djnavigator.spotify.model.SpotifySearchResults;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import dev.nos.djnavigator.spotify.model.id.SpotifyPlaylistId;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.nos.djnavigator.spotify.client.request.SpotifyThreading.executeAsync;

@Service
public class SpotifyQueriesAsync {

    private final SpotifyApi spotifyApi;

    @Autowired
    SpotifyQueriesAsync(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    public CompletableFuture<SpotifyTrack> trackWithAudioFeature(SpotifyTrackId spotifyTrackId) {
        final var spotifyTrack = executeAsync((() -> spotifyApi.track(spotifyTrackId)));
        final var audioFeatures = executeAsync((() -> spotifyApi.audioFeatures(spotifyTrackId)));
        return spotifyTrack
                .thenCombine(audioFeatures, SpotifyTrack::withAudioFeatures);
    }

    public CompletableFuture<List<SpotifyTrack>> tracksWithAudioFeatures(SpotifyAlbumId spotifyAlbumId) {
        return executeAsync((() -> spotifyApi.album(spotifyAlbumId)))
                .thenCompose(album -> withAudioFeatures(album.spotifyTracks()));
    }

    public CompletableFuture<SpotifyAlbum> albumWithTracksAndAudioFeatures(SpotifyAlbumId spotifyAlbumId) {
        final var spotifyAlbum = executeAsync((() -> spotifyApi.album(spotifyAlbumId)));
        return spotifyAlbum
                .thenCompose(album -> withAudioFeatures(album.spotifyTracks()))
                .thenCombine(spotifyAlbum, (tracks, album) -> album.withSpotifyTracks(tracks));
    }

    public CompletableFuture<SpotifySearchResults> searchAlbumOrTrack(String query, int limit) {
        return executeAsync((() -> spotifyApi.searchAlbumOrTrack(query, limit)));
    }

    public CompletableFuture<SpotifyAlbum> albumWithTracks(SpotifyAlbumId albumId) {
        return executeAsync((() -> spotifyApi.album(albumId)));
    }

    public CompletableFuture<SpotifyPlaylist> playlist(SpotifyPlaylistId playlistId) {
        return executeAsync((() -> spotifyApi.playlist(playlistId)));
    }


    private CompletableFuture<List<SpotifyTrack>> withAudioFeatures(List<SpotifyTrack> spotifyTracks) {
        return executeAsync(() -> spotifyApi.audioFeatures(spotifyIds(spotifyTracks)))
                .thenApply(audioFeatures -> spotifyTracks.stream()
                        .map(track -> track.withAudioFeatures(audioFeatures.get(track.spotifyId())))
                        .toList()
                );
    }

    private static List<SpotifyTrackId> spotifyIds(List<SpotifyTrack> spotifyTracks) {
        return spotifyTracks
                .stream()
                .map(SpotifyTrack::spotifyId)
                .toList();
    }

}

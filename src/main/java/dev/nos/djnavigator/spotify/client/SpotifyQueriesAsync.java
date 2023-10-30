package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifyPlaylist;
import dev.nos.djnavigator.spotify.model.SpotifySearchResults;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SpotifyQueriesAsync {

    private final SpotifyApiAsync spotifyApiAsync;


    @Autowired
    SpotifyQueriesAsync(SpotifyApiAsync spotifyApiAsync) {
        this.spotifyApiAsync = spotifyApiAsync;
    }

    public CompletableFuture<SpotifyTrack> trackWithAudioFeature(String spotifyTrackId) {
        final var spotifyTrack = spotifyApiAsync.track(spotifyTrackId);
        final var audioFeatures = spotifyApiAsync.audioFeatures(spotifyTrackId);
        return spotifyTrack
                .thenCombine(audioFeatures, SpotifyTrack::withAudioFeatures);
    }

    public CompletableFuture<List<SpotifyTrack>> tracksWithAudioFeatures(String spotifyAlbumId) {
        return spotifyApiAsync.album(spotifyAlbumId)
                .thenCompose(album -> withAudioFeatures(album.spotifyTracks()));
    }

    public CompletableFuture<SpotifyAlbum> albumWithTracksAndAudioFeatures(String spotifyAlbumId) {
        final var spotifyAlbum = spotifyApiAsync.album(spotifyAlbumId);
        return spotifyAlbum
                .thenCompose(album -> withAudioFeatures(album.spotifyTracks()))
                .thenCombine(spotifyAlbum, (tracks, album) -> album.withSpotifyTracks(tracks));
    }

    public CompletableFuture<SpotifySearchResults> searchAlbumOrTrack(String query, int limit) {
        return spotifyApiAsync.searchAlbumOrTrack(query, limit);
    }

    public CompletableFuture<SpotifyAlbum> albumWithTracks(String albumId) {
        return spotifyApiAsync.album(albumId);
    }

    public CompletableFuture<SpotifyPlaylist> playlist(String playlistId) {
        return spotifyApiAsync.playlist(playlistId);
    }


    private CompletableFuture<List<SpotifyTrack>> withAudioFeatures(List<SpotifyTrack> spotifyTracks) {
        return spotifyApiAsync.audioFeatures(spotifyIds(spotifyTracks))
                .thenApply(audioFeatures -> spotifyTracks.stream()
                        .map(track -> track.withAudioFeatures(audioFeatures.get(track.spotifyId())))
                        .toList()
                );
    }

    private static List<String> spotifyIds(List<SpotifyTrack> spotifyTracks) {
        return spotifyTracks
                .stream()
                .map(SpotifyTrack::spotifyId)
                .toList();
    }

}

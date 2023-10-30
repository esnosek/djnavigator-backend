package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.client.request.album.SpotifyAlbumRequest;
import dev.nos.djnavigator.spotify.client.request.audiofeatures.SpotifyTrackAudioFeaturesRequest;
import dev.nos.djnavigator.spotify.client.request.audiofeatures.SpotifyTracksAudioFeaturesRequest;
import dev.nos.djnavigator.spotify.client.request.playlist.SpotifyPlaylistRequest;
import dev.nos.djnavigator.spotify.client.request.search.SearchAlbumOrTrackRequest;
import dev.nos.djnavigator.spotify.client.request.track.SpotifyTrackRequest;
import dev.nos.djnavigator.spotify.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
class SpotifyApi {

    private final HttpManager httpManager;
    private final SpotifyAuthenticationProvider spotifyAuthenticationProvider;

    @Autowired
    SpotifyApi(HttpManager httpManager,
               SpotifyAuthenticationProvider spotifyAuthenticationProvider) {
        this.httpManager = httpManager;
        this.spotifyAuthenticationProvider = spotifyAuthenticationProvider;
    }

    SpotifySearchResults searchAlbumOrTrack(String query, int limit) {
        return new SearchAlbumOrTrackRequest(accessToken(), query, limit)
                .execute(httpManager);
    }

    SpotifyAlbum album(String albumId) {
        return new SpotifyAlbumRequest(accessToken(), albumId)
                .execute(httpManager);
    }

    SpotifyTrack track(String trackId) {
        return new SpotifyTrackRequest(accessToken(), trackId)
                .execute(httpManager);
    }

    SpotifyPlaylist playlist(String playlistId) {
        return new SpotifyPlaylistRequest(accessToken(), playlistId)
                .execute(httpManager);
    }

    SpotifyTrackAudioFeatures audioFeatures(String trackId) {
        return new SpotifyTrackAudioFeaturesRequest(accessToken(), trackId)
                .execute(httpManager);
    }

    Map<String, SpotifyTrackAudioFeatures> audioFeatures(List<String> tracksIds) {
        return new SpotifyTracksAudioFeaturesRequest(accessToken(), tracksIds)
                .execute(httpManager);
    }

    private String accessToken() {
        return spotifyAuthenticationProvider.getToken();
    }
}


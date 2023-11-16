package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.client.request.SpotifyRequestFactory;
import dev.nos.djnavigator.spotify.model.*;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import dev.nos.djnavigator.spotify.model.id.SpotifyPlaylistId;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
class SpotifyApi {

    private final SpotifyHttpManager spotifyHttpManager;
    private final SpotifyAuthenticationProvider spotifyAuthenticationProvider;
    private final SpotifyRequestFactory requestFactory;

    @Autowired
    SpotifyApi(SpotifyHttpManager spotifyHttpManager,
               SpotifyAuthenticationProvider spotifyAuthenticationProvider,
               SpotifyRequestFactory requestFactory) {
        this.spotifyHttpManager = spotifyHttpManager;
        this.spotifyAuthenticationProvider = spotifyAuthenticationProvider;
        this.requestFactory = requestFactory;
    }

    SpotifySearchResults searchAlbumOrTrack(String query, int limit) {
        return requestFactory
                .searchAlbumOrTrackRequest(accessToken(), query, limit)
                .execute(spotifyHttpManager)
                .getBody();
    }

    SpotifyAlbum album(SpotifyAlbumId albumId) {
        return requestFactory
                .albumRequest(accessToken(), albumId)
                .execute(spotifyHttpManager)
                .getBody();
    }

    SpotifyTrack track(SpotifyTrackId trackId) {
        return requestFactory
                .trackRequest(accessToken(), trackId)
                .execute(spotifyHttpManager)
                .getBody();
    }

    SpotifyPlaylist playlist(SpotifyPlaylistId playlistId) {
        return requestFactory
                .playlistRequest(accessToken(), playlistId)
                .execute(spotifyHttpManager)
                .getBody();
    }

    SpotifyTrackAudioFeatures audioFeatures(SpotifyTrackId trackId) {
        return requestFactory
                .trackAudioFeaturesRequest(accessToken(), trackId)
                .execute(spotifyHttpManager)
                .getBody();
    }

    Map<SpotifyTrackId, SpotifyTrackAudioFeatures> audioFeatures(List<SpotifyTrackId> tracksIds) {
        return requestFactory
                .tracksAudioFeaturesRequest(accessToken(), tracksIds)
                .execute(spotifyHttpManager)
                .getBody();
    }

    SpotifyAudioAnalysis audioAnalysis(SpotifyTrackId spotifyTrackId) {
        return requestFactory
                .audioAnalysis(accessToken(), spotifyTrackId)
                .execute(spotifyHttpManager)
                .getBody();
    }

    private String accessToken() {
        return spotifyAuthenticationProvider.getToken();
    }

}


package dev.nos.djnavigator.spotify.client.request;

import dev.nos.djnavigator.spotify.client.SpotifyClientCredentials;
import dev.nos.djnavigator.spotify.client.request.album.SpotifyAlbumRequest;
import dev.nos.djnavigator.spotify.client.request.audioanalysis.SpotifyGetAudioAnalysisRequest;
import dev.nos.djnavigator.spotify.client.request.audiofeatures.SpotifyTrackAudioFeaturesRequest;
import dev.nos.djnavigator.spotify.client.request.audiofeatures.SpotifyTracksAudioFeaturesRequest;
import dev.nos.djnavigator.spotify.client.request.authentication.SpotifyAuthenticationRequest;
import dev.nos.djnavigator.spotify.client.request.playlist.SpotifyPlaylistRequest;
import dev.nos.djnavigator.spotify.client.request.search.SearchAlbumOrTrackRequest;
import dev.nos.djnavigator.spotify.client.request.track.SpotifyTrackRequest;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import dev.nos.djnavigator.spotify.model.id.SpotifyPlaylistId;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;
import dev.nos.djnavigator.utils.time.Clock;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotifyRequestFactory {

    public SpotifyAuthenticationRequest authenticationRequest(Clock clock, SpotifyClientCredentials credentials) {
        return new SpotifyAuthenticationRequest(clock, credentials);
    }

    public SearchAlbumOrTrackRequest searchAlbumOrTrackRequest(String accessToken, String query, int limit) {
        return new SearchAlbumOrTrackRequest(accessToken, query, limit);
    }

    public SpotifyAlbumRequest albumRequest(String accessToken, SpotifyAlbumId albumId) {
        return new SpotifyAlbumRequest(accessToken, albumId);
    }

    public SpotifyTrackRequest trackRequest(String accessToken, SpotifyTrackId trackId) {
        return new SpotifyTrackRequest(accessToken, trackId);
    }

    public SpotifyPlaylistRequest playlistRequest(String accessToken, SpotifyPlaylistId playlistId) {
        return new SpotifyPlaylistRequest(accessToken, playlistId);
    }

    public SpotifyTrackAudioFeaturesRequest trackAudioFeaturesRequest(String accessToken, SpotifyTrackId trackId) {
        return new SpotifyTrackAudioFeaturesRequest(accessToken, trackId);
    }

    public SpotifyTracksAudioFeaturesRequest tracksAudioFeaturesRequest(String accessToken, List<SpotifyTrackId> tracksIds) {
        return new SpotifyTracksAudioFeaturesRequest(accessToken, tracksIds);
    }

    public SpotifyGetAudioAnalysisRequest audioAnalysis(String accessToken, SpotifyTrackId spotifyTrackId) {
        return new SpotifyGetAudioAnalysisRequest(accessToken, spotifyTrackId);
    }
}

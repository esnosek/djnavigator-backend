package dev.nos.djnavigator.spotify.client.request;

import dev.nos.djnavigator.spotify.client.SpotifyClientCredentials;
import dev.nos.djnavigator.spotify.client.request.album.SpotifyAlbumRequest;
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
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

class SpotifyRequestFactoryTest {

    private final SpotifyRequestFactory requestFactory = new SpotifyRequestFactory();

    @Test
    public void should_authenticationRequest_create_request() {
        // given
        var clock = mock(Clock.class);
        var credentials = mock(SpotifyClientCredentials.class);
        var expectedRequest = new SpotifyAuthenticationRequest(clock, credentials);

        // when
        var result = requestFactory.authenticationRequest(clock, credentials);

        // then
        assertThat(result, is(expectedRequest));
    }

    @Test
    public void should_searchAlbumOrTrackRequest_create_request() {
        // given
        var accessToken = "tokenValue";
        var expectedRequest = new SearchAlbumOrTrackRequest(accessToken, "query", 11);

        // when
        var result = requestFactory.searchAlbumOrTrackRequest(accessToken, "query", 11);

        // then
        assertThat(result, is(expectedRequest));
    }

    @Test
    public void should_albumRequest_create_request() {
        // given
        var accessToken = "tokenValue";
        var albumId = SpotifyAlbumId.from("123");
        var expectedRequest = new SpotifyAlbumRequest(accessToken, albumId);

        // when
        var result = requestFactory.albumRequest(accessToken, albumId);

        // then
        assertThat(result, is(expectedRequest));
    }

    @Test
    public void should_trackRequest_create_request() {
        // given
        var accessToken = "tokenValue";
        var trackId = SpotifyTrackId.from("123");
        var expectedRequest = new SpotifyTrackRequest(accessToken, trackId);

        // when
        var result = requestFactory.trackRequest(accessToken, trackId);

        // then
        assertThat(result, is(expectedRequest));
    }

    @Test
    public void should_playlistRequest_create_request() {
        // given
        var accessToken = "tokenValue";
        var playlistId = SpotifyPlaylistId.from("123");
        var expectedRequest = new SpotifyPlaylistRequest(accessToken, playlistId);

        // when
        var result = requestFactory.playlistRequest(accessToken, playlistId);

        // then
        assertThat(result, is(expectedRequest));
    }

    @Test
    public void should_trackAudioFeaturesRequest_create_request() {
        // given
        var accessToken = "tokenValue";
        var trackId = SpotifyTrackId.from("123");
        var expectedRequest = new SpotifyTrackAudioFeaturesRequest(accessToken, trackId);

        // when
        var result = requestFactory.trackAudioFeaturesRequest(accessToken, trackId);

        // then
        assertThat(result, is(expectedRequest));
    }

    @Test
    public void should_tracksAudioFeaturesRequest_create__request() {
        // given
        var accessToken = "tokenValue";
        var trackId1 = SpotifyTrackId.from("123");
        var trackId2 = SpotifyTrackId.from("456");
        var expectedRequest = new SpotifyTracksAudioFeaturesRequest(accessToken, List.of(trackId1, trackId2));

        // when
        var result = requestFactory.tracksAudioFeaturesRequest(accessToken, List.of(trackId1, trackId2));

        // then
        assertThat(result, is(expectedRequest));
    }
}
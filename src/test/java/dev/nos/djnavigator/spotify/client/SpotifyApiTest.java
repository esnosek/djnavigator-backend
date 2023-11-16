package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.client.request.SpotifyRequestFactory;
import dev.nos.djnavigator.spotify.client.request.SpotifyResponse;
import dev.nos.djnavigator.spotify.client.request.album.SpotifyAlbumRequest;
import dev.nos.djnavigator.spotify.client.request.audiofeatures.SpotifyTrackAudioFeaturesRequest;
import dev.nos.djnavigator.spotify.client.request.audiofeatures.SpotifyTracksAudioFeaturesRequest;
import dev.nos.djnavigator.spotify.client.request.playlist.SpotifyPlaylistRequest;
import dev.nos.djnavigator.spotify.client.request.search.SearchAlbumOrTrackRequest;
import dev.nos.djnavigator.spotify.client.request.track.SpotifyTrackRequest;
import dev.nos.djnavigator.spotify.model.*;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import dev.nos.djnavigator.spotify.model.id.SpotifyPlaylistId;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class SpotifyApiTest {

    private final SpotifyHttpManager spotifyHttpManager = mock(SpotifyHttpManager.class);
    private final SpotifyAuthenticationProvider spotifyAuthenticationProvider = mock(SpotifyAuthenticationProvider.class);
    private final SpotifyRequestFactory requestFactory = mock(SpotifyRequestFactory.class);

    private final SpotifyApi spotifyApi = new SpotifyApi(
            spotifyHttpManager,
            spotifyAuthenticationProvider,
            requestFactory
    );

    @Test
    public void should_searchAlbumOrTrack_return_search_results() {
        // given
        var request = mock(SearchAlbumOrTrackRequest.class);
        var spotifyResponse = mock(SpotifyResponse.class);
        var expectedResult = mock(SpotifySearchResults.class);

        given(spotifyAuthenticationProvider.getToken())
                .willReturn("tokenValue");
        given(requestFactory.searchAlbumOrTrackRequest("tokenValue", "query", 11))
                .willReturn(request);
        given(request.execute(spotifyHttpManager))
                .willReturn(spotifyResponse);
        given(spotifyResponse.getBody())
                .willReturn(expectedResult);

        // when
        var result = spotifyApi.searchAlbumOrTrack("query", 11);

        // then
        assertThat(result, is(expectedResult));
        verify(spotifyAuthenticationProvider).getToken();
        verify(requestFactory).searchAlbumOrTrackRequest("tokenValue", "query", 11);
        verify(request).execute(spotifyHttpManager);
        verify(spotifyResponse).getBody();
        verifyNoMoreInteractions(spotifyAuthenticationProvider, requestFactory, request, spotifyResponse);
    }

    @Test
    public void should_album_return_spotify_album() {
        // given
        var request = mock(SpotifyAlbumRequest.class);
        var spotifyResponse = mock(SpotifyResponse.class);
        var expectedResult = mock(SpotifyAlbum.class);
        var albumId = SpotifyAlbumId.from("123");

        given(spotifyAuthenticationProvider.getToken())
                .willReturn("tokenValue");
        given(requestFactory.albumRequest("tokenValue", albumId))
                .willReturn(request);
        given(request.execute(spotifyHttpManager))
                .willReturn(spotifyResponse);
        given(spotifyResponse.getBody())
                .willReturn(expectedResult);

        // when
        var result = spotifyApi.album(albumId);

        // then
        assertThat(result, is(expectedResult));
        verify(spotifyAuthenticationProvider).getToken();
        verify(requestFactory).albumRequest("tokenValue", albumId);
        verify(request).execute(spotifyHttpManager);
        verify(spotifyResponse).getBody();
        verifyNoMoreInteractions(spotifyAuthenticationProvider, requestFactory, request, spotifyResponse);
    }

    @Test
    public void should_track_return_spotify_track() {
        // given
        var request = mock(SpotifyTrackRequest.class);
        var spotifyResponse = mock(SpotifyResponse.class);
        var expectedResult = mock(SpotifyTrack.class);
        var trackId = SpotifyTrackId.from("123");

        given(spotifyAuthenticationProvider.getToken())
                .willReturn("tokenValue");
        given(requestFactory.trackRequest("tokenValue", trackId))
                .willReturn(request);
        given(request.execute(spotifyHttpManager))
                .willReturn(spotifyResponse);
        given(spotifyResponse.getBody())
                .willReturn(expectedResult);

        // when
        var result = spotifyApi.track(trackId);

        // then
        assertThat(result, is(expectedResult));
        verify(spotifyAuthenticationProvider).getToken();
        verify(requestFactory).trackRequest("tokenValue", trackId);
        verify(request).execute(spotifyHttpManager);
        verify(spotifyResponse).getBody();
        verifyNoMoreInteractions(spotifyAuthenticationProvider, requestFactory, request, spotifyResponse);
    }

    @Test
    public void should_playlist_return_spotify_playlist() {
        // given
        var request = mock(SpotifyPlaylistRequest.class);
        var spotifyResponse = mock(SpotifyResponse.class);
        var expectedResult = mock(SpotifyPlaylist.class);
        var playlistId = SpotifyPlaylistId.from("123");

        given(spotifyAuthenticationProvider.getToken())
                .willReturn("tokenValue");
        given(requestFactory.playlistRequest("tokenValue", playlistId))
                .willReturn(request);
        given(request.execute(spotifyHttpManager))
                .willReturn(spotifyResponse);
        given(spotifyResponse.getBody())
                .willReturn(expectedResult);

        // when
        var result = spotifyApi.playlist(playlistId);

        // then
        assertThat(result, is(expectedResult));
        verify(spotifyAuthenticationProvider).getToken();
        verify(requestFactory).playlistRequest("tokenValue", playlistId);
        verify(request).execute(spotifyHttpManager);
        verify(spotifyResponse).getBody();
        verifyNoMoreInteractions(spotifyAuthenticationProvider, requestFactory, request, spotifyResponse);
    }

    @Test
    public void should_audioFeatures_return_spotify_track_audio_features() {
        // given
        var request = mock(SpotifyTrackAudioFeaturesRequest.class);
        var spotifyResponse = mock(SpotifyResponse.class);
        var expectedResult = mock(SpotifyTrackAudioFeatures.class);
        var trackId = SpotifyTrackId.from("123");

        given(spotifyAuthenticationProvider.getToken())
                .willReturn("tokenValue");
        given(requestFactory.trackAudioFeaturesRequest("tokenValue", trackId))
                .willReturn(request);
        given(request.execute(spotifyHttpManager))
                .willReturn(spotifyResponse);
        given(spotifyResponse.getBody())
                .willReturn(expectedResult);

        // when
        var result = spotifyApi.audioFeatures(trackId);

        // then
        assertThat(result, is(expectedResult));
        verify(spotifyAuthenticationProvider).getToken();
        verify(requestFactory).trackAudioFeaturesRequest("tokenValue", trackId);
        verify(request).execute(spotifyHttpManager);
        verify(spotifyResponse).getBody();
        verifyNoMoreInteractions(spotifyAuthenticationProvider, requestFactory, request, spotifyResponse);
    }

    @Test
    public void should_audioFeatures_return_map_of_spotify_tracks_audio_features() {
        // given
        var request = mock(SpotifyTracksAudioFeaturesRequest.class);
        var spotifyResponse = mock(SpotifyResponse.class);
        var audioFeatures1 = mock(SpotifyTrackAudioFeatures.class);
        var audioFeatures2 = mock(SpotifyTrackAudioFeatures.class);
        var trackId1 = SpotifyTrackId.from("123");
        var trackId2 = SpotifyTrackId.from("456");
        var expectedResult = Map.of(
                trackId1, audioFeatures1,
                trackId2, audioFeatures2
        );

        given(spotifyAuthenticationProvider.getToken())
                .willReturn("tokenValue");
        given(requestFactory.tracksAudioFeaturesRequest("tokenValue", List.of(trackId1, trackId2)))
                .willReturn(request);
        given(request.execute(spotifyHttpManager))
                .willReturn(spotifyResponse);
        given(spotifyResponse.getBody())
                .willReturn(expectedResult);

        // when
        var result = spotifyApi.audioFeatures(List.of(trackId1, trackId2));

        // then
        assertThat(result, is(expectedResult));
        verify(spotifyAuthenticationProvider).getToken();
        verify(requestFactory).tracksAudioFeaturesRequest("tokenValue", List.of(trackId1, trackId2));
        verify(request).execute(spotifyHttpManager);
        verify(spotifyResponse).getBody();
        verifyNoMoreInteractions(spotifyAuthenticationProvider, requestFactory, request, spotifyResponse);
    }
}
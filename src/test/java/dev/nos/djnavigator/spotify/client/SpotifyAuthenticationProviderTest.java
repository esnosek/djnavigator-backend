package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.client.request.SpotifyRequestFactory;
import dev.nos.djnavigator.spotify.client.request.SpotifyResponse;
import dev.nos.djnavigator.spotify.client.request.authentication.SpotifyAuthenticationRequest;
import dev.nos.djnavigator.spotify.config.SpotifyToken;
import dev.nos.djnavigator.spotify.config.SpotifyTokenRepository;
import dev.nos.djnavigator.utils.time.Clock;
import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.spotify.config.SpotifyToken.SPOTIFY_TOKEN_KEY;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class SpotifyAuthenticationProviderTest {

    private final Clock clock = mock(Clock.class);
    private final SpotifyHttpManager spotifyHttpManager = mock(SpotifyHttpManager.class);
    private final SpotifyClientCredentials credentials = new SpotifyClientCredentials("clientId", "clientSecret");
    private final SpotifyRequestFactory requestFactory = mock(SpotifyRequestFactory.class);
    private final SpotifyTokenRepository tokenRepository = mock(SpotifyTokenRepository.class);

    @Test
    void should_getToken_return_token_and_not_execute_authentication_request_when_token_exists() {
        // given
        var authenticationProvider = new SpotifyAuthenticationProvider(
                clock,
                spotifyHttpManager,
                credentials,
                requestFactory,
                tokenRepository);
        var spotifyToken = mock(SpotifyToken.class);

        given(tokenRepository.find(SPOTIFY_TOKEN_KEY))
                .willReturn(of(spotifyToken));
        given(spotifyToken.needToRefresh(clock))
                .willReturn(false);
        given(spotifyToken.value())
                .willReturn("tokenValue");

        // when
        var tokenValue = authenticationProvider.getToken();

        // then
        assertThat(tokenValue, is("tokenValue"));
        verify(tokenRepository).find(SPOTIFY_TOKEN_KEY);
        verify(spotifyToken).needToRefresh(clock);
        verify(spotifyToken).value();
        verifyNoMoreInteractions(tokenRepository, spotifyToken);
        verifyNoInteractions(clock, spotifyHttpManager, requestFactory);
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_getToken_execute_authentication_request_when_token_needs_to_be_refreshed() {
        // given
        var authenticationProvider = new SpotifyAuthenticationProvider(
                clock,
                spotifyHttpManager,
                credentials,
                requestFactory,
                tokenRepository);
        var spotifyResponse = mock(SpotifyResponse.class);
        var spotifyToken = mock(SpotifyToken.class);
        var spotifyAuthenticationRequest = mock(SpotifyAuthenticationRequest.class);

        given(tokenRepository.find(SPOTIFY_TOKEN_KEY))
                .willReturn(of(spotifyToken));
        given(spotifyToken.needToRefresh(clock))
                .willReturn(true);
        given(requestFactory.authenticationRequest(clock, credentials))
                .willReturn(spotifyAuthenticationRequest);
        given(spotifyAuthenticationRequest.execute(spotifyHttpManager))
                .willReturn(spotifyResponse);
        given(spotifyResponse.getBody())
                .willReturn(spotifyToken);
        given(spotifyToken.value())
                .willReturn("tokenValue");

        // when
        var tokenValue = authenticationProvider.getToken();

        // then
        assertThat(tokenValue, is("tokenValue"));

        verify(tokenRepository).find(SPOTIFY_TOKEN_KEY);
        verify(spotifyToken).needToRefresh(clock);
        verify(requestFactory).authenticationRequest(clock, credentials);
        verify(spotifyAuthenticationRequest).execute(spotifyHttpManager);
        verify(spotifyResponse).getBody();
        verify(tokenRepository).set(SPOTIFY_TOKEN_KEY, spotifyToken);
        verify(spotifyToken).value();
        verifyNoInteractions(clock);
        verifyNoMoreInteractions(requestFactory, tokenRepository,
                spotifyAuthenticationRequest, spotifyToken, spotifyResponse);
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_getToken_execute_authentication_request_when_token_not_exists() {
        // given
        var authenticationProvider = new SpotifyAuthenticationProvider(
                clock,
                spotifyHttpManager,
                credentials,
                requestFactory,
                tokenRepository);
        var spotifyResponse = mock(SpotifyResponse.class);
        var spotifyToken = mock(SpotifyToken.class);
        var spotifyAuthenticationRequest = mock(SpotifyAuthenticationRequest.class);

        given(tokenRepository.find(SPOTIFY_TOKEN_KEY))
                .willReturn(empty());
        given(requestFactory.authenticationRequest(clock, credentials))
                .willReturn(spotifyAuthenticationRequest);
        given(spotifyAuthenticationRequest.execute(spotifyHttpManager))
                .willReturn(spotifyResponse);
        given(spotifyResponse.getBody())
                .willReturn(spotifyToken);
        given(spotifyToken.value())
                .willReturn("tokenValue");

        // when
        var tokenValue = authenticationProvider.getToken();

        // then
        assertThat(tokenValue, is("tokenValue"));

        verify(tokenRepository).find(SPOTIFY_TOKEN_KEY);
        verify(requestFactory).authenticationRequest(clock, credentials);
        verify(spotifyAuthenticationRequest).execute(spotifyHttpManager);
        verify(spotifyResponse).getBody();
        verify(tokenRepository).set(SPOTIFY_TOKEN_KEY, spotifyToken);
        verify(spotifyToken).value();
        verifyNoInteractions(clock);
        verifyNoMoreInteractions(requestFactory, tokenRepository,
                spotifyAuthenticationRequest, spotifyToken, spotifyResponse);
    }
}
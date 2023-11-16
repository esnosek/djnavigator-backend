package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.client.request.SpotifyRequestFactory;
import dev.nos.djnavigator.spotify.config.SpotifyToken;
import dev.nos.djnavigator.spotify.config.SpotifyTokenRepository;
import dev.nos.djnavigator.utils.time.Clock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.nos.djnavigator.spotify.config.SpotifyToken.SPOTIFY_TOKEN_KEY;

@Service
@Slf4j
class SpotifyAuthenticationProvider {

    private final Clock clock;
    private final SpotifyHttpManager spotifyHttpManager;
    private final SpotifyClientCredentials credentials;
    private final SpotifyRequestFactory requestFactory;
    private final SpotifyTokenRepository tokenRepository;

    @Autowired
    SpotifyAuthenticationProvider(Clock clock,
                                  SpotifyHttpManager spotifyHttpManager,
                                  SpotifyClientCredentials credentials,
                                  SpotifyRequestFactory requestFactory,
                                  SpotifyTokenRepository tokenRepository) {
        this.clock = clock;
        this.credentials = credentials;
        this.spotifyHttpManager = spotifyHttpManager;
        this.requestFactory = requestFactory;
        this.tokenRepository = tokenRepository;
    }

    String getToken() {
        return tokenRepository
                .find(SPOTIFY_TOKEN_KEY)
                .filter(token -> !token.needToRefresh(clock))
                .orElseGet(this::requestTokenAndSave)
                .value();
    }

    private SpotifyToken requestTokenAndSave() {
        log.info("Spotify access token has been requested");
        final var spotifyToken = requestToken();
        tokenRepository.set(SPOTIFY_TOKEN_KEY, spotifyToken);
        return spotifyToken;
    }

    private SpotifyToken requestToken() {
        return requestFactory
                .authenticationRequest(clock, credentials)
                .execute(spotifyHttpManager)
                .getBody();
    }
}

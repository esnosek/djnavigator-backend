package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.client.request.authentication.SpotifyAuthenticationRequest;
import dev.nos.djnavigator.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SpotifyAuthenticationProvider {

    private final Clock clock;
    private final HttpManager httpManager;
    private final SpotifyClientCredentials credentials;
    private volatile OAuthToken token;

    @Autowired
    SpotifyAuthenticationProvider(Clock clock,
                                  HttpManager httpManager,
                                  SpotifyClientCredentials credentials) {
        this.clock = clock;
        this.credentials = credentials;
        this.httpManager = httpManager;
    }

    String getToken() {
        final var now = clock.instant();
        if (token == null || token.needToRefresh(now)) {
            synchronized (OAuthToken.class) {
                if (token == null || token.needToRefresh(now))
                    token = requestToken();
            }
        }
        return token.value();
    }

    private OAuthToken requestToken() {
        return new SpotifyAuthenticationRequest(clock, credentials)
                .execute(httpManager);
    }
}

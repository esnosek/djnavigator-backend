package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.client.request.authentication.SpotifyAuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class SpotifyAuthenticationProvider {

    private final HttpManager httpManager;
    private final SpotifyClientCredentials credentials;
    private volatile OAuthToken token;

    @Autowired
    SpotifyAuthenticationProvider(HttpManager httpManager, SpotifyClientCredentials credentials) {
        this.credentials = credentials;
        this.httpManager = httpManager;
    }

    String getToken() {
        if (token == null || token.needToRefresh()) {
            synchronized (OAuthToken.class) {
                if (token == null || token.needToRefresh())
                    token = requestToken();
            }
        }
        return token.value();
    }

    private OAuthToken requestToken() {
        return new SpotifyAuthenticationRequest(credentials)
                .execute(httpManager);
    }
}

package dev.nos.djnavigator.spotify.client;

import java.util.Date;

import static java.time.Instant.now;

public record OAuthToken(String token, Date expirationDate) {

    String value() {
        return token;
    }

    boolean needToRefresh() {
        return Date
                .from(now().plusSeconds(60))
                .after(expirationDate);
    }
}

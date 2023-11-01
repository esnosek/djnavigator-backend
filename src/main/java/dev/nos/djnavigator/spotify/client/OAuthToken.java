package dev.nos.djnavigator.spotify.client;

import java.time.Instant;
import java.util.Date;

public record OAuthToken(String token, Date expirationDate) {

    String value() {
        return token;
    }

    boolean needToRefresh(Instant now) {
        return Date
                .from(now.plusSeconds(60))
                .after(expirationDate);
    }
}

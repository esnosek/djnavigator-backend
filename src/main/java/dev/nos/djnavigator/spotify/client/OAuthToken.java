package dev.nos.djnavigator.spotify.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@ToString
class OAuthToken {

    @Getter
    private final String token;
    private final Date expirationDate;

    boolean isExpired() {
        return Date.from(Instant.now().plusSeconds(60))
                .after(expirationDate);
    }
}

package dev.nos.djnavigator.spotify.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.nos.djnavigator.utils.time.Clock;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpotifyToken implements Serializable {

    public final static String SPOTIFY_TOKEN_KEY = "spotify_token_key";

    @JsonProperty
    private final String accessToken;
    @JsonProperty
    private final Date expirationDate;

    @JsonCreator
    public SpotifyToken(@JsonProperty("accessToken") String accessToken,
                        @JsonProperty("expirationDate") Date expirationDate) {
        this.accessToken = accessToken;
        this.expirationDate = expirationDate;
    }

    public boolean needToRefresh(Clock clock) {
        return Date
                .from(clock.instant().plusSeconds(60))
                .after(expirationDate);
    }

    public String value() {
        return accessToken;
    }
}

package dev.nos.djnavigator.spotify.client.request.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.SpotifyClientCredentials;
import dev.nos.djnavigator.spotify.client.request.SpotifyPostRequest;
import dev.nos.djnavigator.spotify.config.SpotifyToken;
import dev.nos.djnavigator.utils.time.Clock;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;

import java.net.URI;
import java.util.Date;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@EqualsAndHashCode(callSuper = false)
public class SpotifyAuthenticationRequest extends SpotifyPostRequest<SpotifyToken> {

    private final static String URI_PATH = "https://accounts.spotify.com/api/token";

    private final Clock clock;
    private final SpotifyClientCredentials credentials;

    public SpotifyAuthenticationRequest(Clock clock, SpotifyClientCredentials credentials) {
        this.clock = clock;
        this.credentials = credentials;
    }

    @Override
    public Function<JsonNode, SpotifyToken> responseMapper() {
        return jsonNode -> {
            final var accessToken = jsonNode.get("access_token").asText();
            final var expiresIn = jsonNode.get("expires_in").asLong();
            return new SpotifyToken(
                    accessToken,
                    Date.from(clock.instant().plusSeconds(expiresIn))
            );
        };
    }

    @Override
    public URI uri() {
        return URI.create(URI_PATH);
    }

    @Override
    public HttpHeaders httpHeaders() {
        final var auth = credentials.clientId() + ":" + credentials.clientSecret();
        final var encodedAuth = encodeBase64(auth.getBytes(US_ASCII), false);
        final var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Basic " + new String(encodedAuth));
        return httpHeaders;
    }

    @Override
    public LinkedMultiValueMap<Object, Object> bodyParams() {
        final var bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("grant_type", "client_credentials");
        return bodyParams;
    }

}

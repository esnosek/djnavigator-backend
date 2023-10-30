package dev.nos.djnavigator.spotify.client.request.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.OAuthToken;
import dev.nos.djnavigator.spotify.client.SpotifyClientCredentials;
import dev.nos.djnavigator.spotify.client.request.SpotifyPostRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;

import java.net.URI;
import java.util.Date;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.time.Instant.now;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

public class SpotifyAuthenticationRequest extends SpotifyPostRequest<OAuthToken> {

    private final static String URI_PATH = "https://accounts.spotify.com/api/token";

    private final SpotifyClientCredentials credentials;

    public SpotifyAuthenticationRequest(SpotifyClientCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public Function<JsonNode, OAuthToken> responseMapper() {
        return jsonNode -> {
            final var accessToken = jsonNode.get("access_token").asText();
            final var expiresIn = jsonNode.get("expires_in").asLong();
            return new OAuthToken(
                    accessToken,
                    Date.from(now().plusSeconds(expiresIn))
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

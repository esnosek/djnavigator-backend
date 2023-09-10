package dev.nos.djnavigator.spotify.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.apache.tomcat.util.codec.binary.Base64.encodeBase64;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
class SpotifyTokenRetriever {

    private final static URI AUTHORIZATION_URI = URI.create("https://accounts.spotify.com/api/token");

    private final RestTemplate restTemplate;
    private final SpotifyCredentials spotifyCredentials;
    private OAuthToken token = null;

    @Autowired
    private SpotifyTokenRetriever(RestTemplate restTemplate, SpotifyCredentials spotifyCredentials) {
        this.spotifyCredentials = spotifyCredentials;
        this.restTemplate = restTemplate;
    }

    public String getToken() {
        if (token == null || token.isExpired()) {
            token = requestToken();
            System.out.println(token);
        }
        return token.getToken();
    }

    private OAuthToken requestToken() {
        final var bodyParamMap = new LinkedMultiValueMap<>();
        bodyParamMap.add("grant_type", "client_credentials");

        final var accessTokenResponse = restTemplate.exchange(
                AUTHORIZATION_URI,
                HttpMethod.POST,
                new HttpEntity<>(bodyParamMap, authorizationHeaders()),
                AccessTokenResponse.class
        );

        return Optional.ofNullable(accessTokenResponse.getBody())
                .map(res -> new OAuthToken(
                        res.accessToken,
                        Date.from(Instant.now().plusSeconds(res.expiresIn))
                ))
                .orElseThrow();
    }

    private HttpHeaders authorizationHeaders() {
        final var auth = spotifyCredentials.clientId() + ":" + spotifyCredentials.clientSecret();
        final var encodedAuth = encodeBase64(auth.getBytes(US_ASCII), false);
        final var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Basic " + new String(encodedAuth));
        return httpHeaders;
    }

    @Builder
    @Jacksonized
    private record AccessTokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") long expiresIn) {
    }
}

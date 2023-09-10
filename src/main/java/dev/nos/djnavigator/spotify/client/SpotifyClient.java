package dev.nos.djnavigator.spotify.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class SpotifyClient {

    private final SpotifyTokenRetriever spotifyTokenRetriever;
    private final RestTemplate restTemplate;

    @Autowired
    private SpotifyClient(SpotifyTokenRetriever spotifyTokenRetriever, RestTemplate restTemplate) {
        this.spotifyTokenRetriever = spotifyTokenRetriever;
        this.restTemplate = restTemplate;
    }

    JsonNode saveGet(URI uri) {
        return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(authorizationHeaders()), JsonNode.class).getBody();
    }

    private HttpHeaders authorizationHeaders() {
        final var httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + spotifyTokenRetriever.getToken());
        httpHeaders.setContentType(APPLICATION_JSON);
        return httpHeaders;
    }

}

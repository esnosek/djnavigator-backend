package dev.nos.djnavigator.spotify.client;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.request.SpotifyGetRequest;
import dev.nos.djnavigator.spotify.client.request.SpotifyPostRequest;
import dev.nos.djnavigator.spotify.client.request.SpotifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class SpotifyHttpManager {

    private final RestTemplate restTemplate;

    private static final int BLOCKING_QUEUE_SIZE = 1000;
    private final static BlockingQueue<SpotifyRequest<?>> SPOTIFY_REQUESTS_QUEUE =
            new LinkedBlockingQueue<>(BLOCKING_QUEUE_SIZE);

    @Autowired
    SpotifyHttpManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<JsonNode> executeGet(SpotifyGetRequest<?> request) {
        return restTemplate
                .exchange(
                        request.uri(),
                        HttpMethod.GET,
                        new HttpEntity<>(request.httpHeaders()),
                        JsonNode.class
                );
    }

    public ResponseEntity<JsonNode> executePost(SpotifyPostRequest<?> request) {
        return restTemplate
                .exchange(
                        request.uri(),
                        HttpMethod.POST,
                        new HttpEntity<>(request.bodyParams(), request.httpHeaders()),
                        JsonNode.class
                );
    }
}

package dev.nos.djnavigator.spotify.client;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.request.SpotifyGetRequest;
import dev.nos.djnavigator.spotify.client.request.SpotifyPostRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpManager {

    private final RestTemplate restTemplate;

    HttpManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JsonNode executeGet(SpotifyGetRequest<?> request) {
        return restTemplate
                .exchange(
                        request.uri(),
                        HttpMethod.GET,
                        new HttpEntity<>(request.httpHeaders()),
                        JsonNode.class
                )
                .getBody();
    }

    public JsonNode executePost(SpotifyPostRequest<?> request) {
        return restTemplate
                .exchange(
                        request.uri(),
                        HttpMethod.POST,
                        new HttpEntity<>(request.bodyParams(), request.httpHeaders()),
                        JsonNode.class
                )
                .getBody();
    }
}

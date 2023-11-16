package dev.nos.djnavigator.spotify.client.request;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

import java.util.function.Function;

public class SpotifyResponse<T> {

    private final ResponseEntity<JsonNode> response;
    private final Function<JsonNode, T> responseMapper;

    public SpotifyResponse(ResponseEntity<JsonNode> response, Function<JsonNode, T> responseMapper) {
        this.response = response;
        this.responseMapper = responseMapper;
    }

    public T getBody() {
        return responseMapper.apply(response.getBody());
    }

}

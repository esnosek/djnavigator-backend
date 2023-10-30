package dev.nos.djnavigator.spotify.client.request;

import dev.nos.djnavigator.spotify.client.HttpManager;
import org.springframework.util.LinkedMultiValueMap;

public abstract class SpotifyPostRequest<T> extends SpotifyRequest<T> {

    public abstract LinkedMultiValueMap<Object, Object> bodyParams();

    @Override
    public T execute(HttpManager httpManager) {
        final var response = httpManager.executePost(this);
        return responseMapper().apply(response);
    }
}

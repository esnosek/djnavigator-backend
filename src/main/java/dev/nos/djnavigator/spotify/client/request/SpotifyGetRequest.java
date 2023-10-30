package dev.nos.djnavigator.spotify.client.request;

import dev.nos.djnavigator.spotify.client.HttpManager;

public abstract class SpotifyGetRequest<T> extends SpotifyRequest<T> {

    @Override
    public T execute(HttpManager httpManager) {
        final var response = httpManager.executeGet(this);
        return responseMapper().apply(response);
    }
}

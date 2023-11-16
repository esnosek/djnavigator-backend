package dev.nos.djnavigator.spotify.client.request;

import dev.nos.djnavigator.spotify.client.SpotifyHttpManager;
import org.springframework.util.LinkedMultiValueMap;

public abstract class SpotifyPostRequest<T> extends SpotifyRequest<T> {

    public abstract LinkedMultiValueMap<Object, Object> bodyParams();

    @Override
    public SpotifyResponse<T> execute(SpotifyHttpManager spotifyHttpManager) {
        final var response = spotifyHttpManager.executePost(this);
        return new SpotifyResponse<>(response, responseMapper());
    }
}

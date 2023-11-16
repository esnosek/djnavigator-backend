package dev.nos.djnavigator.spotify.client.request;

import dev.nos.djnavigator.spotify.client.SpotifyHttpManager;

public abstract class SpotifyGetRequest<T> extends SpotifyRequest<T> {

    @Override
    public SpotifyResponse<T> execute(SpotifyHttpManager spotifyHttpManager) {
        final var response = spotifyHttpManager.executeGet(this);
        return new SpotifyResponse<>(response, responseMapper());
    }
}

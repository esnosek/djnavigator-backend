package dev.nos.djnavigator.spotify.client.request;

import org.springframework.http.HttpHeaders;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public abstract class SpotifyAuthorizedGetRequest<T> extends SpotifyGetRequest<T> {

    private final String token;

    protected SpotifyAuthorizedGetRequest(String token) {
        this.token = token;
    }

    @Override
    public HttpHeaders httpHeaders() {
        final var httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + this.token);
        httpHeaders.setContentType(APPLICATION_JSON);
        return httpHeaders;
    }

}

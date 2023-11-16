package dev.nos.djnavigator.spotify.client.request;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.SpotifyHttpManager;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.util.function.Function;

public abstract class SpotifyRequest<T> {

    public abstract URI uri();

    public abstract Function<JsonNode, T> responseMapper();

    public abstract HttpHeaders httpHeaders();

    public abstract SpotifyResponse<T> execute(SpotifyHttpManager spotifyHttpManager);
}

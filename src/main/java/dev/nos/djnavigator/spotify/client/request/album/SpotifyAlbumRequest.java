package dev.nos.djnavigator.spotify.client.request.album;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.request.SpotifyAuthorizedGetRequest;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import lombok.EqualsAndHashCode;

import java.net.URI;
import java.util.function.Function;

import static java.lang.String.format;

@EqualsAndHashCode(callSuper = true)
public class SpotifyAlbumRequest extends SpotifyAuthorizedGetRequest<SpotifyAlbum> {

    private static final String URI_PATH = "https://api.spotify.com/v1/albums/%s";

    private final SpotifyAlbumId albumId;

    public SpotifyAlbumRequest(String token, SpotifyAlbumId albumId) {
        super(token);
        this.albumId = albumId;
    }

    @Override
    public Function<JsonNode, SpotifyAlbum> responseMapper() {
        return new SpotifyAlbumConverter();
    }

    @Override
    public URI uri() {
        return URI.create(format(URI_PATH, albumId));
    }
}

package dev.nos.djnavigator.spotify.client.request.playlist;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.request.SpotifyAuthorizedGetRequest;
import dev.nos.djnavigator.spotify.model.SpotifyPlaylist;

import java.net.URI;
import java.util.function.Function;

import static java.lang.String.format;

public class SpotifyPlaylistRequest extends SpotifyAuthorizedGetRequest<SpotifyPlaylist> {

    private static final String URI_PATH = "https://api.spotify.com/v1/playlists/%s";

    private final String playlistId;

    public SpotifyPlaylistRequest(String token, String playlistId) {
        super(token);
        this.playlistId = playlistId;
    }

    @Override
    public Function<JsonNode, SpotifyPlaylist> responseMapper() {
        return new SpotifyPlaylistConverter();
    }

    @Override
    public URI uri() {
        return URI.create(format(URI_PATH, playlistId));
    }
}

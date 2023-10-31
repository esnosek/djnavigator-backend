package dev.nos.djnavigator.spotify.client.request.track;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.request.SpotifyAuthorizedGetRequest;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;

import java.net.URI;
import java.util.function.Function;

import static java.lang.String.format;

public class SpotifyTrackRequest extends SpotifyAuthorizedGetRequest<SpotifyTrack> {

    private static final String URI_PATH = "https://api.spotify.com/v1/tracks/%s";

    private final SpotifyTrackId trackId;

    public SpotifyTrackRequest(String token, SpotifyTrackId trackId) {
        super(token);
        this.trackId = trackId;
    }

    @Override
    public Function<JsonNode, SpotifyTrack> responseMapper() {
        return new SpotifyTrackConverter();
    }

    @Override
    public URI uri() {
        return URI.create(format(URI_PATH, trackId));
    }
}

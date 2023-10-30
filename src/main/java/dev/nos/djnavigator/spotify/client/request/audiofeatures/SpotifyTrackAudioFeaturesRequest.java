package dev.nos.djnavigator.spotify.client.request.audiofeatures;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.request.SpotifyAuthorizedGetRequest;
import dev.nos.djnavigator.spotify.model.SpotifyTrackAudioFeatures;

import java.net.URI;
import java.util.function.Function;

import static java.lang.String.format;

public class SpotifyTrackAudioFeaturesRequest extends SpotifyAuthorizedGetRequest<SpotifyTrackAudioFeatures> {

    private static final String URI_PATH = "https://api.spotify.com/v1/audio-features/%s";

    private final String trackId;

    public SpotifyTrackAudioFeaturesRequest(String token, String trackId) {
        super(token);
        this.trackId = trackId;
    }

    @Override
    public Function<JsonNode, SpotifyTrackAudioFeatures> responseMapper() {
        return new SpotifyTrackAudioFeaturesConverter();
    }

    @Override
    public URI uri() {
        return URI.create(format(URI_PATH, trackId));
    }
}

package dev.nos.djnavigator.spotify.client.request.audioanalysis;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.request.SpotifyAuthorizedGetRequest;
import dev.nos.djnavigator.spotify.model.SpotifyAudioAnalysis;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;

import java.net.URI;
import java.util.function.Function;

import static java.lang.String.format;

public class SpotifyGetAudioAnalysisRequest extends SpotifyAuthorizedGetRequest<SpotifyAudioAnalysis> {

    private static final String URI_PATH = "https://api.spotify.com/v1/audio-analysis/%s";

    private final SpotifyTrackId trackId;

    public SpotifyGetAudioAnalysisRequest(String token, SpotifyTrackId trackId) {
        super(token);
        this.trackId = trackId;
    }

    @Override
    public URI uri() {
        return URI.create(format(URI_PATH, trackId));
    }

    @Override
    public Function<JsonNode, SpotifyAudioAnalysis> responseMapper() {
        return jsonNode -> new SpotifyAudioAnalysis(trackId, jsonNode);

    }
}

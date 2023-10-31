package dev.nos.djnavigator.spotify.client.request.audiofeatures;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.request.SpotifyAuthorizedGetRequest;
import dev.nos.djnavigator.spotify.model.SpotifyTrackAudioFeatures;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;

public class SpotifyTracksAudioFeaturesRequest extends SpotifyAuthorizedGetRequest<Map<SpotifyTrackId, SpotifyTrackAudioFeatures>> {

    private static final String URI_PATH = "https://api.spotify.com/v1/audio-features?ids=%s";

    private final Collection<SpotifyTrackId> tracksIds;

    public SpotifyTracksAudioFeaturesRequest(String token, Collection<SpotifyTrackId> tracksIds) {
        super(token);
        this.tracksIds = tracksIds;
    }

    @Override
    public Function<JsonNode, Map<SpotifyTrackId, SpotifyTrackAudioFeatures>> responseMapper() {
        return new SpotifyTracksAudioFeaturesConverter();
    }

    @Override
    public URI uri() {
        final var trackIdsAsString = tracksIds.stream().map(SpotifyTrackId::id).toList();
        return URI.create(format(URI_PATH, String.join(",", trackIdsAsString)));
    }
}

package dev.nos.djnavigator.spotify.client.request.search;

import com.fasterxml.jackson.databind.JsonNode;
import dev.nos.djnavigator.spotify.client.request.SpotifyAuthorizedGetRequest;
import dev.nos.djnavigator.spotify.model.SpotifySearchResults;
import lombok.EqualsAndHashCode;

import java.net.URI;
import java.util.function.Function;

import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

@EqualsAndHashCode(callSuper = true)
public class SearchAlbumOrTrackRequest extends SpotifyAuthorizedGetRequest<SpotifySearchResults> {

    private static final String URI_PATH = "https://api.spotify.com/v1/search?q=%s&type=%s&limit=%s";

    private final String query;
    private final int limit;

    public SearchAlbumOrTrackRequest(String token, String query, int limit) {
        super(token);
        this.query = query;
        this.limit = limit;
    }

    @Override
    public Function<JsonNode, SpotifySearchResults> responseMapper() {
        return new SpotifySearchResultsConverter();
    }

    @Override
    public URI uri() {
        return URI.create(format(URI_PATH, encode(query, UTF_8), "album,track", limit));
    }
}

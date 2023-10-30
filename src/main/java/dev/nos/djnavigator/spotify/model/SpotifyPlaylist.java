package dev.nos.djnavigator.spotify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpotifyPlaylist(
        String id,
        List<SpotifyTrack> tracks
) {
}
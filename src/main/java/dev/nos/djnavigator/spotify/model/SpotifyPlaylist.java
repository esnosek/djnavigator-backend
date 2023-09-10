package dev.nos.djnavigator.spotify.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Jacksonized
public record SpotifyPlaylist(
        String id,
        List<SpotifyTrack> tracks
) {
}
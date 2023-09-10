package dev.nos.djnavigator.spotify.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Builder
@Jacksonized
public record SpotifyAlbumWithTracks(
        SpotifyAlbum spotifyAlbum,
        List<SpotifyTrack> spotifyTracks
) {
    public List<String> tracksIds() {
        return spotifyTracks().stream()
                .map(SpotifyTrack::spotifyId)
                .collect(toList());
    }
}

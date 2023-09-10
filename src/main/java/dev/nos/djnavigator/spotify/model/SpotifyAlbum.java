package dev.nos.djnavigator.spotify.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Optional;

@Builder
@Jacksonized
public record SpotifyAlbum(
        String spotifyId,
        String name,
        List<String> artists,
        Optional<List<SpotifyTrack>> spotifyTracks,
        String imagePath
) {
    public SpotifyAlbum withSpotifyTracks(List<SpotifyTrack> spotifyTracks) {
        return copy()
                .spotifyTracks(Optional.of(spotifyTracks))
                .build();
    }

    private SpotifyAlbum.SpotifyAlbumBuilder copy() {
        return SpotifyAlbum.builder()
                .spotifyId(this.spotifyId)
                .name(this.name)
                .artists(this.artists)
                .spotifyTracks(this.spotifyTracks)
                .imagePath(this.imagePath);
    }
}

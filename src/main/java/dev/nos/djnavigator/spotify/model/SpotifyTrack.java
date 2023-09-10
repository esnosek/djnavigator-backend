package dev.nos.djnavigator.spotify.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

@Builder
@Jacksonized
public record SpotifyTrack(
        String spotifyId,
        String name,
        List<String> artists,
        Optional<SpotifyTrackAudioFeatures> audioFeatures,
        Optional<SpotifyAlbum> spotifyAlbum,
        String imagePath
) {

    public SpotifyTrack withAudioFeatures(SpotifyTrackAudioFeatures audioFeatures) {
        return copy()
                .audioFeatures(of(audioFeatures))
                .build();
    }

    public SpotifyTrack withSpotifyAlbum(SpotifyAlbum spotifyAlbum) {
        return copy()
                .spotifyAlbum(of(spotifyAlbum))
                .build();
    }

    private SpotifyTrack.SpotifyTrackBuilder copy() {
        return SpotifyTrack.builder()
                .spotifyId(this.spotifyId)
                .name(this.name)
                .artists(this.artists)
                .audioFeatures(this.audioFeatures)
                .spotifyAlbum(this.spotifyAlbum)
                .imagePath(this.imagePath);
    }
}

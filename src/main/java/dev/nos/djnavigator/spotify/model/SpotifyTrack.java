package dev.nos.djnavigator.spotify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SpotifyTrack(
        String spotifyId,
        String name,
        List<String> artists,
        SpotifyTrackAudioFeatures audioFeatures,
        SpotifyAlbum spotifyAlbum,
        String imagePath
) {

    public SpotifyTrack withAudioFeatures(SpotifyTrackAudioFeatures audioFeatures) {
        return copy()
                .audioFeatures(audioFeatures)
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

    public String spotifyAlbumId() {
        return spotifyAlbum.spotifyId();
    }
}

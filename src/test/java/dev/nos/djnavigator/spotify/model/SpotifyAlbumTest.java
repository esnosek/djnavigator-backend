package dev.nos.djnavigator.spotify.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.nos.djnavigator.TestData.spotifyAlbum;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

class SpotifyAlbumTest {

    @Test
    void should_withSpotifyTracks_return_spotifyAlbum_with_spotifyTracks() {
        // given
        var spotifyAlbumBuilder = spotifyAlbum();
        var spotifyAlbum = spotifyAlbumBuilder.build();
        var spotifyTrack1 = mock(SpotifyTrack.class);
        var spotifyTrack2 = mock(SpotifyTrack.class);

        var spotifyAlbumWithSpotifyTracks = spotifyAlbumBuilder
                .spotifyTracks(List.of(spotifyTrack1, spotifyTrack2))
                .build();

        // when
        var actual = spotifyAlbum.withSpotifyTracks(List.of(spotifyTrack1, spotifyTrack2));

        // then
        assertThat(
                actual,
                is(spotifyAlbumWithSpotifyTracks)
        );
    }

}
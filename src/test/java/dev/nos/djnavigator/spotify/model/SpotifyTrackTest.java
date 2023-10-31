package dev.nos.djnavigator.spotify.model;

import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.TestData.spotifyTrack;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

class SpotifyTrackTest {

    @Test
    void should_withAudioFeatures_return_spotifyTrack_with_audioFeatures() {
        // given
        var spotifyTrackBuilder = spotifyTrack();
        var spotifyTrack = spotifyTrackBuilder.build();
        var audioFeatures = mock(SpotifyTrackAudioFeatures.class);

        var spotifyTrackWithAudioFeatures = spotifyTrackBuilder
                .audioFeatures(audioFeatures)
                .build();

        // when
        var actual = spotifyTrack.withAudioFeatures(audioFeatures);

        // then
        assertThat(
                actual,
                is(spotifyTrackWithAudioFeatures)
        );
    }
}
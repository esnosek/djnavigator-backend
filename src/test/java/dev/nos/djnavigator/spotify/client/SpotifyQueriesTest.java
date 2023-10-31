package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.spotify.model.SpotifyTrackAudioFeatures;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SpotifyQueriesTest {

    private final SpotifyApi spotifyApi = mock(SpotifyApi.class);

    private final SpotifyQueries spotifyQueries = new SpotifyQueries(spotifyApi);

    @Test
    void should_trackWithAudioFeature_return_spotifyTrack() {
        // given
        var spotifyTrackId = SpotifyTrackId.randomId();
        var spotifyTrack = mock(SpotifyTrack.class);
        var audioFeatures = mock(SpotifyTrackAudioFeatures.class);
        var spotifyTrackWithAudioFeatures = mock(SpotifyTrack.class);

        given(spotifyApi.track(spotifyTrackId)).willReturn(spotifyTrack);
        given(spotifyApi.audioFeatures(spotifyTrackId)).willReturn(audioFeatures);
        given(spotifyTrack.withAudioFeatures(audioFeatures)).willReturn(spotifyTrackWithAudioFeatures);

        // when
        var actualSpotifyTrack = spotifyQueries.trackWithAudioFeature(spotifyTrackId);

        assertThat(
                actualSpotifyTrack,
                is(spotifyTrackWithAudioFeatures)
        );
    }

}
package dev.nos.djnavigator.collection.model.converter;

import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.utils.time.Clock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static dev.nos.djnavigator.TestData.album;
import static dev.nos.djnavigator.TestData.spotifyTrack;
import static dev.nos.djnavigator.utils.ConvertersUtils.toTrackSpotifyId;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TrackFromSpotifyTrackConverterTest {

    private final Clock clock = mock(Clock.class);

    private final TrackFromSpotifyTrackConverter trackFromSpotifyTrackConverter = new TrackFromSpotifyTrackConverter(clock);

    @Test
    void should_ToTrack_SpotifyTrack_ReturnTrack() {
        // given
        var spotifyTrack = spotifyTrack().build();
        var album = album().build();
        var now = LocalDateTime.now();
        var createdDate = now.truncatedTo(MILLIS);

        given(clock.now()).willReturn(now);

        // when
        var track = trackFromSpotifyTrackConverter.apply(spotifyTrack, album);

        // then
        assertThat(track)
                .isNotNull()
                .returns(true, from(exp -> exp.getId() != null))
                .returns(createdDate, from(Track::getCreatedDate))
                .returns(spotifyTrack.name(), from(Track::getName))
                .returns(spotifyTrack.artists(), from(Track::getArtists))
                .returns(album, from(Track::getAlbum))
                .returns(spotifyTrack.audioFeatures().tempo(), from(Track::getTempo))
                .returns(toTrackSpotifyId(spotifyTrack.spotifyId()), from(Track::getSpotifyId));
    }
}
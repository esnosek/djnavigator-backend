package dev.nos.djnavigator.controller.converter;

import dev.nos.djnavigator.collection.controller.converter.model.TrackConverter;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.time.Clock;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static dev.nos.djnavigator.TestData.*;
import static dev.nos.djnavigator.collection.controller.converter.model.ConvertersUtils.toTrackSpotifyId;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TrackConverterTest {

    private final Clock clock = mock(Clock.class);

    private final TrackConverter trackConverter = new TrackConverter(clock);

    @Test
    void should_ToTrack_TrackCreateDto_ReturnTrack() {
        // given
        var trackCreateDto = trackCreateDto().build();
        var album = album().build();
        var now = LocalDateTime.now();
        var createdDate = now.truncatedTo(MILLIS);

        given(clock.now()).willReturn(now);

        // when
        var track = trackConverter.createTrack(trackCreateDto, album);

        // then
        assertThat(track)
                .isNotNull()
                .returns(true, from(exp -> exp.getId() != null))
                .returns(createdDate, from(Track::getCreatedDate))
                .returns(trackCreateDto.name(), from(Track::getName))
                .returns(trackCreateDto.artists(), from(Track::getArtists))
                .returns(album, from(Track::getAlbum))
                .returns(trackCreateDto.tempo().orElseThrow(), from(Track::getTempo))
                .returns(null, from(Track::getSpotifyId));
    }

    @Test
    void should_ToTrack_TrackCreateDto_ReturnTrack_WhenOptionalValuesNotProvided() {
        // given
        var trackCreateDto = trackCreateDto()
                .tempo(empty())
                .build();
        var now = LocalDateTime.now();

        given(clock.now()).willReturn(now);

        // when
        var track = trackConverter.createTrack(trackCreateDto, album().build());

        // then
        assertThat(track)
                .isNotNull()
                .returns(null, from(Track::getTempo));
    }

    @Test
    void should_ToTrack_SpotifyTrack_ReturnTrack() {
        // given
        var spotifyTrack = spotifyTrack().build();
        var album = album().build();
        var now = LocalDateTime.now();
        var createdDate = now.truncatedTo(MILLIS);

        given(clock.now()).willReturn(now);

        // when
        var track = trackConverter.createTrack(spotifyTrack, album);

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
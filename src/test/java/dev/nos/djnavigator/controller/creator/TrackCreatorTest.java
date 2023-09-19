package dev.nos.djnavigator.controller.creator;

import dev.nos.djnavigator.controller.creator.model.TrackCreator;
import dev.nos.djnavigator.model.Track;
import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.TestData.spotifyTrack;
import static dev.nos.djnavigator.TestData.trackCreateDto;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class TrackCreatorTest {

    private final TrackCreator trackCreator = new TrackCreator();

    @Test
    void should_ToTrack_TrackCreateDto_ReturnTrack() {
        // given
        final var trackCreateDto = trackCreateDto().build();

        //when
        final var track = trackCreator.toTrack(trackCreateDto);

        //then
        assertThat(track)
                .isNotNull()
                .returns(true, from(exp -> exp.getId() != null))
                .returns(true, from(exp -> exp.getCreatedDate() != null))
                .returns(trackCreateDto.name(), from(Track::getName))
                .returns(trackCreateDto.artists(), from(Track::getArtists))
                .returns(null, from(Track::getAlbum))
                .returns(trackCreateDto.tempo().orElseThrow(), from(Track::getTempo))
                .returns(null, from(Track::getSpotifyId));
    }

    @Test
    void should_ToTrack_TrackCreateDto_ReturnTrack_WhenOptionalValuesNotProvided() {
        //given
        final var trackCreateDto = trackCreateDto()
                .tempo(empty())
                .build();

        //when
        final var track = trackCreator.toTrack(trackCreateDto);

        //then
        assertThat(track)
                .isNotNull()
                .returns(null, from(Track::getTempo));
    }

    @Test
    void should_ToTrack_SpotifyTrack_ReturnTrack() {
        // given
        final var spotifyTrack = spotifyTrack().build();

        //when
        final var track = trackCreator.toTrack(spotifyTrack);

        //then
        assertThat(track)
                .isNotNull()
                .returns(true, from(exp -> exp.getId() != null))
                .returns(true, from(exp -> exp.getCreatedDate() != null))
                .returns(spotifyTrack.name(), from(Track::getName))
                .returns(spotifyTrack.artists(), from(Track::getArtists))
                .returns(null, from(Track::getAlbum))
                .returns(spotifyTrack.audioFeatures().orElseThrow().tempo(), from(Track::getTempo))
                .returns(null, from(Track::getAlbum))
                .returns(spotifyTrack.spotifyId(), from(Track::getSpotifyId));
    }

    @Test
    void should_ToTrack_SpotifyTrack_ReturnTrack_WhenOptionalValuesNotProvided() {
        // given
        final var spotifyTrack = spotifyTrack()
                .audioFeatures(empty())
                .build();

        //when
        final var track = trackCreator.toTrack(spotifyTrack);

        //then
        assertThat(track)
                .isNotNull()
                .returns(null, from(Track::getTempo));
    }
}
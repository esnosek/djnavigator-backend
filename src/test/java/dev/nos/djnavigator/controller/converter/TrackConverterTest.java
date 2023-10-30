package dev.nos.djnavigator.controller.converter;

import dev.nos.djnavigator.collection.controller.converter.model.TrackConverter;
import dev.nos.djnavigator.collection.model.Track;
import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.TestData.*;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class TrackConverterTest {

    private final TrackConverter trackConverter = new TrackConverter();

    @Test
    void should_ToTrack_TrackCreateDto_ReturnTrack() {
        // given
        final var trackCreateDto = trackCreateDto().build();
        final var album = album().build();

        //when
        final var track = trackConverter.createTrack(trackCreateDto, album);

        //then
        assertThat(track)
                .isNotNull()
                .returns(true, from(exp -> exp.getId() != null))
                .returns(true, from(exp -> exp.getCreatedDate() != null))
                .returns(trackCreateDto.name(), from(Track::getName))
                .returns(trackCreateDto.artists(), from(Track::getArtists))
                .returns(album, from(Track::getAlbum))
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
        final var track = trackConverter.createTrack(trackCreateDto, album().build());

        //then
        assertThat(track)
                .isNotNull()
                .returns(null, from(Track::getTempo));
    }

    @Test
    void should_ToTrack_SpotifyTrack_ReturnTrack() {
        // given
        final var spotifyTrack = spotifyTrack().build();
        final var album = album().build();
        //when
        final var track = trackConverter.createTrack(spotifyTrack, album);

        //then
        assertThat(track)
                .isNotNull()
                .returns(true, from(exp -> exp.getId() != null))
                .returns(true, from(exp -> exp.getCreatedDate() != null))
                .returns(spotifyTrack.name(), from(Track::getName))
                .returns(spotifyTrack.artists(), from(Track::getArtists))
                .returns(album, from(Track::getAlbum))
                .returns(spotifyTrack.audioFeatures().tempo(), from(Track::getTempo))
                .returns(spotifyTrack.spotifyId(), from(Track::getSpotifyId));
    }
}
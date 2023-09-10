package dev.nos.djnavigator.controller.converters;

import dev.nos.djnavigator.dto.TrackView;
import dev.nos.djnavigator.model.Track;
import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.TestData.*;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class TrackConverterTest {

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

    @Test
    void should_ToTrackView_Track_ReturnTrackView() {
        //given
        final var track = track().build();

        //when
        final var trackView = trackCreator.toTrackView(track);

        //then
        assertThat(trackView)
                .isNotNull()
                .returns(track.getId(), from(TrackView::id))
                .returns(track.getCreatedDate(), from(TrackView::createdDate))
                .returns(track.getName(), from(TrackView::name))
                .returns(track.getArtists(), from(TrackView::artists))
                .returns(null, from(TrackView::album))
                .returns(track.getTempo(), from(TrackView::tempo))
                .returns(track.getSpotifyId(), from(TrackView::spotifyId));
    }

    @Test
    void should_ToTrackView_Track_ReturnTrackView_WhenOptionalValuesNotProvided() {
        //given
        final var track = track()
                .spotifyId(null)
                .build();

        //when
        final var trackView = trackCreator.toTrackView(track);

        //then
        assertThat(trackView)
                .isNotNull()
                .returns(null, from(TrackView::spotifyId));
    }
}
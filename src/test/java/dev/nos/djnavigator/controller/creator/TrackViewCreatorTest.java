package dev.nos.djnavigator.controller.creator;

import dev.nos.djnavigator.controller.creator.view.TrackViewCreator;
import dev.nos.djnavigator.dto.TrackView;
import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.TestData.track;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class TrackViewCreatorTest {

    private final TrackViewCreator trackCreator = new TrackViewCreator();

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
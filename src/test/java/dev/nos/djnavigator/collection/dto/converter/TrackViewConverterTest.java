package dev.nos.djnavigator.collection.dto.converter;

import dev.nos.djnavigator.collection.dto.TrackView;
import org.junit.jupiter.api.Test;

import static dev.nos.djnavigator.TestData.track;
import static dev.nos.djnavigator.collection.dto.converter.TrackViewConverter.trackView;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class TrackViewConverterTest {

    @Test
    void should_ToTrackView_Track_ReturnTrackView() {
        // given
        var track = track().build();

        // when
        var trackView = trackView(track, false);

        // then
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
        // given
        var track = track()
                .spotifyId(null)
                .build();

        // when
        var trackView = trackView(track, false);

        // then
        assertThat(trackView)
                .isNotNull()
                .returns(null, from(TrackView::spotifyId));
    }
}
package dev.nos.djnavigator.controller.creator.view;

import dev.nos.djnavigator.dto.TrackView;
import dev.nos.djnavigator.model.Track;

public class TrackViewCreator {

    public TrackView toTrackView(Track track) {
        return TrackView.builder()
                .id(track.getId())
                .createdDate(track.getCreatedDate())
                .name(track.getName())
                .artists(track.getArtists())
                .tempo(track.getTempo())
                .spotifyId(track.getSpotifyId())
                .build();
    }
}

package dev.nos.djnavigator.controller.creator.view;

import dev.nos.djnavigator.dto.TrackView;
import dev.nos.djnavigator.model.Track;

public class TrackViewWithAlbumCreator {

    private final TrackViewCreator trackViewCreator;
    private final AlbumViewCreator albumViewCreator;

    public TrackViewWithAlbumCreator(TrackViewCreator trackViewCreator,
                                     AlbumViewCreator albumViewCreator) {
        this.trackViewCreator = trackViewCreator;
        this.albumViewCreator = albumViewCreator;
    }

    public TrackView trackViewWithAlbum(Track track) {
        return trackViewCreator
                .toTrackView(track)
                .withAlbum(albumViewCreator.toAlbumView(track.getAlbum()));
    }
}

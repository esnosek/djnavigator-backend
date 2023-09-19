package dev.nos.djnavigator.controller.creator.view;

import dev.nos.djnavigator.dto.AlbumView;
import dev.nos.djnavigator.dto.TrackView;
import dev.nos.djnavigator.model.Album;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Service
public class AlbumViewWithTracksCreator {

    private final TrackViewCreator trackViewCreator;
    private final AlbumViewCreator albumViewCreator;

    public AlbumViewWithTracksCreator(TrackViewCreator trackViewCreator,
                                      AlbumViewCreator albumViewCreator) {
        this.trackViewCreator = trackViewCreator;
        this.albumViewCreator = albumViewCreator;
    }

    public AlbumView albumViewWithTracks(Album album) {
        return albumViewCreator
                .toAlbumView(album)
                .withTracks(trackViewsFrom(album));
    }

    private List<TrackView> trackViewsFrom(Album album) {
        return ofNullable(album.getTracks())
                .map(Collection::stream)
                .map(tracks -> tracks.map(trackViewCreator::toTrackView).toList())
                .orElse(emptyList());
    }

}

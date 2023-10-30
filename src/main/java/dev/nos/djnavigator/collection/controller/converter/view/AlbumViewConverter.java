package dev.nos.djnavigator.collection.controller.converter.view;

import dev.nos.djnavigator.collection.dto.AlbumView;
import dev.nos.djnavigator.collection.dto.TrackView;
import dev.nos.djnavigator.collection.model.Album;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static dev.nos.djnavigator.collection.controller.converter.view.TrackViewConverter.toTrackView;
import static java.util.Optional.ofNullable;


public class AlbumViewConverter {
    public static AlbumView toAlbumView(Album album, boolean includeTracks) {
        final var albumViewBuilder = albumViewBuilder(album);
        if (includeTracks)
            albumViewBuilder.tracks(trackViewsFrom(album));
        return albumViewBuilder.build();
    }

    private static List<TrackView> trackViewsFrom(Album album) {
        return ofNullable(album.getTracks())
                .map(Collection::stream)
                .map(tracks -> tracks
                        .map(track -> toTrackView(track, false))
                        .toList()
                )
                .orElseGet(Collections::emptyList);
    }

    private static AlbumView.AlbumViewBuilder albumViewBuilder(Album album) {
        return AlbumView.builder()
                .id(album.getId().id())
                .createdDate(album.getCreatedDate())
                .name(album.getName())
                .artists(album.getArtists())
                .spotifyId(album.getSpotifyId())
                .imagePath(album.getImagePath());
    }
}

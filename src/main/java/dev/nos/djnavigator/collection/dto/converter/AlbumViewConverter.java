package dev.nos.djnavigator.collection.dto.converter;

import dev.nos.djnavigator.collection.dto.AlbumView;
import dev.nos.djnavigator.collection.dto.TrackView;
import dev.nos.djnavigator.collection.model.Album;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static dev.nos.djnavigator.collection.dto.converter.TrackViewConverter.trackView;
import static java.util.Optional.ofNullable;


public class AlbumViewConverter {

    public static AlbumView albumView(Album album, boolean includeTracks) {
        final var albumViewBuilder = albumViewBuilder(album);
        if (includeTracks)
            albumViewBuilder.tracks(trackViewsFrom(album));
        return albumViewBuilder.build();
    }

    private static List<TrackView> trackViewsFrom(Album album) {
        return ofNullable(album.getTracks())
                .map(Collection::stream)
                .map(tracks -> tracks
                        .map(track -> trackView(track, false))
                        .toList()
                )
                .orElseGet(Collections::emptyList);
    }

    private static AlbumView.AlbumViewBuilder albumViewBuilder(Album album) {
        return AlbumView.builder()
                .id(album.getId())
                .createdDate(album.getCreatedDate())
                .name(album.getName())
                .artists(album.getArtists())
                .spotifyId(album.getSpotifyId())
                .imagePath(album.getImagePath());
    }
}

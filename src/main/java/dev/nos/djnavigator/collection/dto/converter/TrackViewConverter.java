package dev.nos.djnavigator.collection.dto.converter;

import dev.nos.djnavigator.collection.dto.TrackView;
import dev.nos.djnavigator.collection.model.Track;

import static dev.nos.djnavigator.collection.dto.converter.AlbumViewConverter.albumView;

public class TrackViewConverter {

    public static TrackView trackView(Track track, boolean includeAlbum) {
        final var trackViewBuilder = trackViewBuilder(track);
        if (includeAlbum)
            trackViewBuilder.album(
                    albumView(track.getAlbum(), false)
            );
        return trackViewBuilder.build();
    }

    private static TrackView.TrackViewBuilder trackViewBuilder(Track track) {
        return TrackView.builder()
                .id(track.getId())
                .createdDate(track.getCreatedDate())
                .name(track.getName())
                .artists(track.getArtists())
                .tempo(track.getTempo())
                .spotifyId(track.getSpotifyId());
    }
}

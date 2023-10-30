package dev.nos.djnavigator.collection.controller.converter.view;

import dev.nos.djnavigator.collection.dto.TrackView;
import dev.nos.djnavigator.collection.model.Track;
import org.springframework.stereotype.Service;

@Service
public class TrackViewConverter {

    public static TrackView toTrackView(Track track, boolean includeAlbum) {
        final var trackViewBuilder = trackViewBuilder(track);
        if (includeAlbum)
            trackViewBuilder.album(
                    AlbumViewConverter.toAlbumView(track.getAlbum(), false)
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

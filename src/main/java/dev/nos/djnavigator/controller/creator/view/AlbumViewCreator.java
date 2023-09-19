package dev.nos.djnavigator.controller.creator.view;

import dev.nos.djnavigator.dto.AlbumView;
import dev.nos.djnavigator.model.Album;
import org.springframework.stereotype.Service;

@Service
public class AlbumViewCreator {
    public AlbumView toAlbumView(Album album) {
        return AlbumView.builder()
                .id(album.getId())
                .createdDate(album.getCreatedDate())
                .name(album.getName())
                .artists(album.getArtists())
                .spotifyId(album.getSpotifyId())
                .imagePath(album.getImagePath())
                .build();
    }
}

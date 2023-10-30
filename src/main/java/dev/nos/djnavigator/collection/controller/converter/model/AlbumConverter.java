package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.dto.AlbumCreateDto;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AlbumConverter {

    public Album toAlbum(AlbumCreateDto albumDto) {
        return Album.builder()
                .name(albumDto.name())
                .artists(albumDto.artists())
                .build();
    }

    public Album toAlbum(SpotifyAlbum spotifyAlbum) {
        return Album.builder()
                .name(spotifyAlbum.name())
                .artists(spotifyAlbum.artists())
                .spotifyId(spotifyAlbum.spotifyId())
                .imagePath(spotifyAlbum.imagePath())
                .build();
    }

    private LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }
}

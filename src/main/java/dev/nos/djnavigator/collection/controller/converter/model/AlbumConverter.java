package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.dto.AlbumCreateDto;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import org.springframework.stereotype.Service;

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
                .spotifyId(ConvertersUtils.toAlbumSpotifyId(spotifyAlbum.spotifyId()))
                .imagePath(spotifyAlbum.imagePath())
                .build();
    }

}

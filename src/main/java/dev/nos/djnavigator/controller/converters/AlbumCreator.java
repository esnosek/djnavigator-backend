package dev.nos.djnavigator.controller.converters;

import dev.nos.djnavigator.dto.AlbumCreateDto;
import dev.nos.djnavigator.dto.AlbumView;
import dev.nos.djnavigator.model.Album;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import org.springframework.stereotype.Service;
import org.unbrokendome.base62.Base62;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.util.UUID.randomUUID;

@Service
public class AlbumCreator {

    public Album createAlbum(AlbumCreateDto albumDto) {
        return Album.builder()
                .id(Base62.encodeUUID(randomUUID()))
                .createdDate(now())
                .name(albumDto.name())
                .artists(albumDto.artists())
                .build();
    }

    public Album createAlbum(SpotifyAlbum spotifyAlbum) {
        return Album.builder()
                .id(Base62.encodeUUID(randomUUID()))
                .createdDate(now())
                .name(spotifyAlbum.name())
                .artists(spotifyAlbum.artists())
                .spotifyId(spotifyAlbum.spotifyId())
                .imagePath(spotifyAlbum.imagePath())
                .build();
    }

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

    private LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
    }
}

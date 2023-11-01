package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.dto.AlbumCreateDto;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.time.temporal.ChronoUnit.MILLIS;

@Service
public class AlbumConverter {

    public final Clock clock;

    @Autowired
    public AlbumConverter(Clock clock) {
        this.clock = clock;
    }

    public Album toAlbum(AlbumCreateDto albumDto) {
        return Album.builder()
                .createdDate(clock.now().truncatedTo(MILLIS))
                .name(albumDto.name())
                .artists(albumDto.artists())
                .build();
    }

    public Album toAlbum(SpotifyAlbum spotifyAlbum) {
        return Album.builder()
                .createdDate(clock.now().truncatedTo(MILLIS))
                .name(spotifyAlbum.name())
                .artists(spotifyAlbum.artists())
                .spotifyId(ConvertersUtils.toAlbumSpotifyId(spotifyAlbum.spotifyId()))
                .imagePath(spotifyAlbum.imagePath())
                .build();
    }

}

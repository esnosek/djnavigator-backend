package dev.nos.djnavigator.collection.model.converter;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.utils.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static dev.nos.djnavigator.utils.ConvertersUtils.toAlbumSpotifyId;
import static java.time.temporal.ChronoUnit.MILLIS;

@Service
public class AlbumFromSpotifyAlbumConverter implements Function<SpotifyAlbum, Album> {

    public final Clock clock;

    @Autowired
    public AlbumFromSpotifyAlbumConverter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Album apply(SpotifyAlbum spotifyAlbum) {
        return Album.builder()
                .createdDate(clock.now().truncatedTo(MILLIS))
                .name(spotifyAlbum.name())
                .artists(spotifyAlbum.artists())
                .spotifyId(toAlbumSpotifyId(spotifyAlbum.spotifyId()))
                .imagePath(spotifyAlbum.imagePath())
                .build();
    }

}

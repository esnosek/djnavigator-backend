package dev.nos.djnavigator;

import dev.nos.djnavigator.dto.AlbumCreateDto;
import dev.nos.djnavigator.dto.TrackCreateDto;
import dev.nos.djnavigator.model.Album;
import dev.nos.djnavigator.model.Track;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.spotify.model.SpotifyTrackAudioFeatures;
import org.unbrokendome.base62.Base62;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;

public class TestData {

    public static Track.TrackBuilder track() {
        return Track.builder()
                .id(Base62.encodeUUID(randomUUID()))
                .createdDate(LocalDateTime.now())
                .name("track name")
                .artists(List.of("artist1", "artist2"))
                .tempo(new BigDecimal("123.123"))
                .spotifyId("spotifyId");
    }

    public static Album.AlbumBuilder album() {
        return Album.builder()
                .id(Base62.encodeUUID(randomUUID()))
                .createdDate(LocalDateTime.now())
                .name("album name")
                .artists(List.of("artist1", "artist2"))
                .spotifyId("spotifyId");
    }


    public static SpotifyTrack.SpotifyTrackBuilder spotifyTrack() {
        return SpotifyTrack.builder()
                .spotifyId("spotifyId")
                .name("spotify track name")
                .artists(List.of("artist1", "artist2"))
                .audioFeatures(of(spotifyTrackAudioFeatures().build()))
                .spotifyAlbum(empty());
    }

    public static SpotifyTrackAudioFeatures.SpotifyTrackAudioFeaturesBuilder spotifyTrackAudioFeatures() {
        return SpotifyTrackAudioFeatures.builder()
                .tempo(new BigDecimal("123.123"));
    }

    public static SpotifyAlbum.SpotifyAlbumBuilder spotifyAlbum() {
        return SpotifyAlbum.builder()
                .spotifyId("spotifyId")
                .name("spotify album name")
                .artists(List.of("artist1", "artist2"))
                .spotifyTracks(empty());
    }

    public static TrackCreateDto.TrackCreateDtoBuilder trackCreateDto() {
        return TrackCreateDto.builder()
                .name("track name")
                .artists(List.of("artist1", "artist2"))
                .albumId("albumId")
                .tempo(of(new BigDecimal("134.134")));
    }

    public static AlbumCreateDto.AlbumCreateDtoBuilder albumCreateDto() {
        return AlbumCreateDto.builder()
                .name("album name")
                .artists(List.of("artist1", "artist2"));
    }
}

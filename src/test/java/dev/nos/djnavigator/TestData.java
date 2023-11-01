package dev.nos.djnavigator;

import dev.nos.djnavigator.collection.dto.AlbumCreateDto;
import dev.nos.djnavigator.collection.dto.TrackCreateDto;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId;
import dev.nos.djnavigator.collection.model.id.TrackSpotifyId;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.spotify.model.SpotifyTrackAudioFeatures;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.of;

public class TestData {

    public static Clock CLOCK = Clock.systemDefaultZone();

    public static void setClock(Clock clock) {
        TestData.CLOCK = clock;
    }

    public static Track.TrackBuilder track() {
        return Track.builder()
                .createdDate(LocalDateTime.now(CLOCK).truncatedTo(MILLIS))
                .name("track name")
                .artists(List.of("artist1", "artist2"))
                .tempo(new BigDecimal("123.123"))
                .spotifyId(TrackSpotifyId.from("spotifyId"));
    }

    public static Album.AlbumBuilder album() {
        return Album.builder()
                .createdDate(LocalDateTime.now(CLOCK).truncatedTo(MILLIS))
                .name("album name")
                .artists(List.of("artist1", "artist2"))
                .spotifyId(AlbumSpotifyId.from("spotifyId"));
    }


    public static SpotifyTrack.SpotifyTrackBuilder spotifyTrack() {
        return SpotifyTrack.builder()
                .spotifyId(SpotifyTrackId.randomId())
                .name("spotify track name")
                .artists(List.of("artist1", "artist2"))
                .audioFeatures(spotifyTrackAudioFeatures().build())
                .spotifyAlbum(spotifyAlbum().build());
    }

    public static SpotifyTrackAudioFeatures.SpotifyTrackAudioFeaturesBuilder spotifyTrackAudioFeatures() {
        return SpotifyTrackAudioFeatures.builder()
                .tempo(new BigDecimal("123.123"));
    }

    public static SpotifyAlbum.SpotifyAlbumBuilder spotifyAlbum() {
        return SpotifyAlbum.builder()
                .spotifyId(SpotifyAlbumId.randomId())
                .name("spotify album name")
                .artists(List.of("artist1", "artist2"))
                .spotifyTracks(List.of());
    }

    public static TrackCreateDto.TrackCreateDtoBuilder trackCreateDto() {
        return TrackCreateDto.builder()
                .name("track name")
                .artists(List.of("artist1", "artist2"))
                .albumId(AlbumId.from("albumId"))
                .tempo(of(new BigDecimal("134.134")));
    }

    public static AlbumCreateDto.AlbumCreateDtoBuilder albumCreateDto() {
        return AlbumCreateDto.builder()
                .name("album name")
                .artists(List.of("artist1", "artist2"));
    }
}

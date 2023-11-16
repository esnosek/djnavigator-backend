package dev.nos.djnavigator.collection.model.converter;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.utils.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

import static dev.nos.djnavigator.utils.ConvertersUtils.toTrackSpotifyId;
import static java.time.temporal.ChronoUnit.MILLIS;

@Service
public class TrackFromSpotifyTrackConverter implements BiFunction<SpotifyTrack, Album, Track> {

    public final Clock clock;

    @Autowired
    public TrackFromSpotifyTrackConverter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Track apply(SpotifyTrack spotifyTrack, Album album) {
        return Track.builder()
                .createdDate(clock.now().truncatedTo(MILLIS))
                .name(spotifyTrack.name())
                .artists(spotifyTrack.artists())
                .spotifyId(toTrackSpotifyId(spotifyTrack.spotifyId()))
                .tempo(spotifyTrack.audioFeatures().tempo())
                .album(album)
                .build();
    }

}

package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.dto.TrackCreateDto;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.nos.djnavigator.collection.controller.converter.model.ConvertersUtils.toTrackSpotifyId;
import static java.time.temporal.ChronoUnit.MILLIS;

@Service
public class TrackConverter {

    public final Clock clock;

    @Autowired
    public TrackConverter(Clock clock) {
        this.clock = clock;
    }

    public Track createTrack(TrackCreateDto trackDto, Album album) {
        return Track.builder()
                .createdDate(clock.now().truncatedTo(MILLIS))
                .name(trackDto.name())
                .artists(trackDto.artists())
                .tempo(trackDto.tempo().orElse(null))
                .album(album)
                .build();
    }

    public Track createTrack(SpotifyTrack spotifyTrack, Album album) {
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

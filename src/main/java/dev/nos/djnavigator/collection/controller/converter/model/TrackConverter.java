package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.dto.TrackCreateDto;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import org.springframework.stereotype.Service;
import org.unbrokendome.base62.Base62;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.UUID.randomUUID;

@Service
public class TrackConverter {

    public Track createTrack(TrackCreateDto trackDto, Album album) {
        return Track.builder()
                .id(Base62.encodeUUID(randomUUID()))
                .createdDate(now())
                .name(trackDto.name())
                .artists(trackDto.artists())
                .tempo(trackDto.tempo().orElse(null))
                .album(album)
                .build();
    }

    public Track createTrack(SpotifyTrack spotifyTrack, Album album) {
        return Track.builder()
                .id(Base62.encodeUUID(randomUUID()))
                .createdDate(now())
                .name(spotifyTrack.name())
                .artists(spotifyTrack.artists())
                .spotifyId(spotifyTrack.spotifyId())
                .tempo(spotifyTrack.audioFeatures().tempo())
                .album(album)
                .build();
    }

    private LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(MILLIS);
    }
}

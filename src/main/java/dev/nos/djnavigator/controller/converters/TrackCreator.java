package dev.nos.djnavigator.controller.converters;

import dev.nos.djnavigator.dto.TrackCreateDto;
import dev.nos.djnavigator.dto.TrackView;
import dev.nos.djnavigator.model.Track;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.spotify.model.SpotifyTrackAudioFeatures;
import org.springframework.stereotype.Service;
import org.unbrokendome.base62.Base62;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.UUID.randomUUID;

@Service
public class TrackCreator {

    public Track toTrack(TrackCreateDto trackDto) {
        return Track.builder()
                .id(Base62.encodeUUID(randomUUID()))
                .createdDate(now())
                .name(trackDto.name())
                .artists(trackDto.artists())
                .tempo(trackDto.tempo().orElse(null))
                .build();
    }

    public Track toTrack(SpotifyTrack spotifyTrack) {
        return Track.builder()
                .id(Base62.encodeUUID(randomUUID()))
                .createdDate(now())
                .name(spotifyTrack.name())
                .artists(spotifyTrack.artists())
                .spotifyId(spotifyTrack.spotifyId())
                .tempo(spotifyTrack.audioFeatures().map(SpotifyTrackAudioFeatures::tempo).orElse(null))
                .build();
    }

    public TrackView toTrackView(Track track) {
        return TrackView.builder()
                .id(track.getId())
                .createdDate(track.getCreatedDate())
                .name(track.getName())
                .artists(track.getArtists())
                .tempo(track.getTempo())
                .spotifyId(track.getSpotifyId())
                .build();
    }

    private LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(MILLIS);
    }
}

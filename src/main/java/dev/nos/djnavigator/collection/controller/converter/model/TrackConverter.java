package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.dto.TrackCreateDto;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import org.springframework.stereotype.Service;

import static dev.nos.djnavigator.collection.controller.converter.model.ConvertersUtils.toTrackSpotifyId;

@Service
public class TrackConverter {

    public Track createTrack(TrackCreateDto trackDto, Album album) {
        return Track.builder()
                .name(trackDto.name())
                .artists(trackDto.artists())
                .tempo(trackDto.tempo().orElse(null))
                .album(album)
                .build();
    }

    public Track createTrack(SpotifyTrack spotifyTrack, Album album) {
        return Track.builder()
                .name(spotifyTrack.name())
                .artists(spotifyTrack.artists())
                .spotifyId(toTrackSpotifyId(spotifyTrack.spotifyId()))
                .tempo(spotifyTrack.audioFeatures().tempo())
                .album(album)
                .build();
    }

}

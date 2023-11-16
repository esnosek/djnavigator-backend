package dev.nos.djnavigator.collection.model.converter;

import dev.nos.djnavigator.collection.dto.TrackCreateDto;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.utils.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

import static java.time.temporal.ChronoUnit.MILLIS;

@Service
public class TrackFromTrackCreateDtoConverter implements BiFunction<TrackCreateDto, Album, Track> {

    private final Clock clock;

    @Autowired
    public TrackFromTrackCreateDtoConverter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Track apply(TrackCreateDto trackDto, Album album) {
        return Track.builder()
                .createdDate(clock.now().truncatedTo(MILLIS))
                .name(trackDto.name())
                .artists(trackDto.artists())
                .tempo(trackDto.tempo().orElse(null))
                .album(album)
                .build();
    }


}

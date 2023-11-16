package dev.nos.djnavigator.collection.model.converter;

import dev.nos.djnavigator.collection.dto.AlbumCreateDto;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.utils.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static java.time.temporal.ChronoUnit.MILLIS;

@Service
public class AlbumFromAlbumCreateDtoConverter implements Function<AlbumCreateDto, Album> {

    public final Clock clock;

    @Autowired
    public AlbumFromAlbumCreateDtoConverter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Album apply(AlbumCreateDto albumDto) {
        return Album.builder()
                .createdDate(clock.now().truncatedTo(MILLIS))
                .name(albumDto.name())
                .artists(albumDto.artists())
                .build();
    }
}

package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.dto.TrackCreateDto;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.collection.model.converter.TrackFromTrackCreateDtoConverter;
import dev.nos.djnavigator.collection.model.event.TrackCreatedEvent;
import dev.nos.djnavigator.collection.model.id.TrackId;
import dev.nos.djnavigator.event.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {

    private final AlbumQueries albumQueries;
    private final TrackQueries trackQueries;
    private final TrackFromTrackCreateDtoConverter trackCreateDtoConverter;
    private final TrackRepository trackRepository;
    private final EventPublisher eventPublisher;

    @Autowired
    public TrackService(AlbumQueries albumQueries,
                        TrackQueries trackQueries,
                        TrackFromTrackCreateDtoConverter trackCreateDtoConverter,
                        TrackRepository trackRepository,
                        EventPublisher eventPublisher) {
        this.albumQueries = albumQueries;
        this.trackQueries = trackQueries;
        this.trackCreateDtoConverter = trackCreateDtoConverter;
        this.trackRepository = trackRepository;
        this.eventPublisher = eventPublisher;
    }

    public Track save(TrackCreateDto trackCreateDto) {
        final var album = albumQueries.getById(trackCreateDto.albumId());
        final var track = trackCreateDtoConverter.apply(trackCreateDto, album);
        return save(track);
    }

    public Track save(Track trackToSave) {
        final var track = trackRepository.save(trackToSave);
        eventPublisher.publishEvent(new TrackCreatedEvent(track));
        return track;
    }

    public void deleteById(TrackId trackId) {
        final var track = trackQueries.getById(trackId);
        trackRepository.deleteById(track.getId());
    }

}

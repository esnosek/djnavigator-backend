package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.dto.AlbumCreateDto;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.converter.AlbumFromAlbumCreateDtoConverter;
import dev.nos.djnavigator.collection.model.event.TrackCreatedEvent;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import dev.nos.djnavigator.event.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {


    private final AlbumRepository albumRepository;
    private final AlbumQueries albumQueries;
    private final AlbumFromAlbumCreateDtoConverter albumCreateDtoConverter;
    private final EventPublisher eventPublisher;

    @Autowired
    public AlbumService(AlbumRepository albumRepository,
                        AlbumQueries albumQueries,
                        AlbumFromAlbumCreateDtoConverter albumCreateDtoConverter,
                        EventPublisher eventPublisher) {
        this.albumRepository = albumRepository;
        this.albumQueries = albumQueries;
        this.albumCreateDtoConverter = albumCreateDtoConverter;
        this.eventPublisher = eventPublisher;
    }

    public Album save(AlbumCreateDto albumCreateDto) {
        final var album = albumCreateDtoConverter.apply(albumCreateDto);
        return save(album);
    }

    public Album save(Album albumToSave) {
        final var album = albumRepository.save(albumToSave);
        publishEventsOnCreation(album);
        return album;
    }

    public void deleteById(AlbumId albumId) {
        final var album = albumQueries.getById(albumId);
        albumRepository.deleteById(album.getId());
    }

    private void publishEventsOnCreation(Album album) {
        album.getTracks()
                .stream()
                .map(TrackCreatedEvent::new)
                .forEach(eventPublisher::publishEvent);
    }
}

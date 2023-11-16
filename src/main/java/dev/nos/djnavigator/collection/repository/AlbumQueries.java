package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.controller.exception.AlbumNotFoundException;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.StreamSupport.stream;

@Service
public class AlbumQueries {

    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumQueries(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Album getById(AlbumId albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
    }

    public Optional<Album> findBySpotifyId(AlbumSpotifyId albumSpotifyId) {
        return albumRepository.findBySpotifyId(albumSpotifyId);
    }

    public List<Album> findAll() {
        return stream(albumRepository.findAll().spliterator(), false)
                .toList();
    }

}

package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.controller.exception.AlbumNotFoundException;
import dev.nos.djnavigator.collection.controller.exception.TrackNotFoundException;
import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import dev.nos.djnavigator.collection.model.id.TrackId;
import dev.nos.djnavigator.collection.model.id.TrackSpotifyId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackQueries {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;

    @Autowired
    public TrackQueries(TrackRepository trackRepository,
                        AlbumRepository albumRepository) {
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
    }

    public Track getBySpotifyId(TrackSpotifyId trackSpotifyId) {
        return trackRepository.findBySpotifyId(trackSpotifyId)
                .orElseThrow(() -> new TrackNotFoundException(trackSpotifyId));
    }

    public Track getById(TrackId trackId) {
        return trackRepository.findById(trackId)
                .orElseThrow(() -> new TrackNotFoundException(trackId));
    }

    public List<Track> findByAlbumId(AlbumId albumId) {
        final var album = getAlbumById(albumId);
        return trackRepository.findByAlbumId(album.getId());
    }

    public Album getAlbumById(AlbumId albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
    }

}

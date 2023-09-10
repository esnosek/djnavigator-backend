package dev.nos.djnavigator.controller.converters;

import dev.nos.djnavigator.controller.exception.AlbumNotFoundException;
import dev.nos.djnavigator.controller.exception.SpotifyAlbumNotFoundException;
import dev.nos.djnavigator.dto.TrackCreateDto;
import dev.nos.djnavigator.model.Album;
import dev.nos.djnavigator.model.Track;
import dev.nos.djnavigator.repository.AlbumRepository;
import dev.nos.djnavigator.spotify.client.SpotifyQueries;
import dev.nos.djnavigator.spotify.client.SpotifyTrackResolver;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import org.springframework.stereotype.Service;

@Service
public class TrackWithAlbumCreator {

    private final TrackCreator trackCreator;
    private final AlbumRepository albumRepository;

    public TrackWithAlbumCreator(TrackCreator trackCreator,
                                 AlbumRepository albumRepository) {
        this.trackCreator = trackCreator;
        this.albumRepository = albumRepository;
    }

    public Track createTrackWithAlbum(TrackCreateDto trackCreateDto) {
        final var album = findAlbumOrThrow(trackCreateDto.albumId());
        return trackCreator
                .toTrack(trackCreateDto)
                .withAlbum(album);
    }

    private Album findAlbumOrThrow(String albumId) {
        return albumRepository
                .findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
    }
}
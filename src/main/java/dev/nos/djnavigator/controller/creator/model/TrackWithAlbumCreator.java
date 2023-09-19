package dev.nos.djnavigator.controller.creator.model;

import dev.nos.djnavigator.controller.exception.AlbumNotFoundException;
import dev.nos.djnavigator.dto.TrackCreateDto;
import dev.nos.djnavigator.model.Album;
import dev.nos.djnavigator.model.Track;
import dev.nos.djnavigator.repository.AlbumRepository;
import dev.nos.djnavigator.spotify.SpotifyTrackResolver;
import dev.nos.djnavigator.spotify.client.SpotifyQueries;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import org.springframework.stereotype.Service;

@Service
public class TrackWithAlbumCreator {

    private final TrackCreator trackCreator;
    private final AlbumRepository albumRepository;
    private final SpotifyTrackResolver spotifyTrackResolver;
    private final AlbumCreator albumCreator;
    private final SpotifyQueries spotifyQueries;

    public TrackWithAlbumCreator(TrackCreator trackCreator,
                                 AlbumRepository albumRepository,
                                 SpotifyTrackResolver spotifyTrackResolver,
                                 AlbumCreator albumCreator,
                                 SpotifyQueries spotifyQueries) {
        this.trackCreator = trackCreator;
        this.albumRepository = albumRepository;
        this.spotifyTrackResolver = spotifyTrackResolver;
        this.albumCreator = albumCreator;
        this.spotifyQueries = spotifyQueries;
    }

    public Track createTrackWithAlbum(TrackCreateDto trackCreateDto) {
        final var album = findAlbumOrThrow(trackCreateDto.albumId());
        return trackCreator
                .toTrack(trackCreateDto)
                .withAlbum(album);
    }

    public Track createTrackWithAlbum(String spotifyTrackId) {
        final var spotifyTrack = spotifyTrackResolver.trackWithAudioFeature(spotifyTrackId);
        final var spotifyAlbumId = spotifyTrack
                .spotifyAlbum()
                .map(SpotifyAlbum::spotifyId)
                .orElseThrow();
        final var album = findAlbumBySpotifyIdOrCreate(spotifyAlbumId);
        return trackCreator
                .toTrack(spotifyTrack)
                .withAlbum(album);
    }

    private Album findAlbumOrThrow(String albumId) {
        return albumRepository
                .findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(albumId));
    }

    private Album findAlbumBySpotifyIdOrCreate(String spotifyAlbumId) {
        return albumRepository
                .findBySpotifyId(spotifyAlbumId)
                .orElseGet(() ->
                        albumCreator.createAlbum(spotifyQueries.getAlbumWithTracks(spotifyAlbumId))
                );
    }
}

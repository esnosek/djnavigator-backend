package dev.nos.djnavigator.controller.converters;

import dev.nos.djnavigator.model.Album;
import dev.nos.djnavigator.repository.AlbumRepository;
import dev.nos.djnavigator.spotify.client.SpotifyQueries;
import dev.nos.djnavigator.spotify.client.SpotifyTrackResolver;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import org.springframework.stereotype.Service;

@Service
public class AlbumWithTrackCreator {
    private final TrackCreator trackCreator;
    private final AlbumCreator albumCreator;
    private final SpotifyQueries spotifyQueries;
    private final SpotifyTrackResolver spotifyTrackResolver;
    private final AlbumRepository albumRepository;

    public AlbumWithTrackCreator(TrackCreator trackCreator,
                                 AlbumCreator albumCreator,
                                 SpotifyQueries spotifyQueries,
                                 SpotifyTrackResolver spotifyTrackResolver,
                                 AlbumRepository albumRepository) {
        this.trackCreator = trackCreator;
        this.albumCreator = albumCreator;
        this.spotifyQueries = spotifyQueries;
        this.spotifyTrackResolver = spotifyTrackResolver;
        this.albumRepository = albumRepository;
    }

    public Album createAlbumWithTracks(String spotifyAlbumId) {
        final var album = findAlbumBySpotifyIdOrCreate(spotifyAlbumId);
        spotifyTrackResolver
                .tracksWithAudioFeatures(spotifyAlbumId)
                .stream()
                .map(spotifyTrack -> trackCreator
                        .toTrack(spotifyTrack)
                        .withAlbum(album))
                .forEach(album::addTrack);
        return album;
    }

    public Album createAlbumWithTrack(String spotifyTrackId) {
        final var spotifyTrack = spotifyTrackResolver.trackWithAudioFeature(spotifyTrackId);
        final var spotifyAlbumId = spotifyTrack
                .spotifyAlbum()
                .map(SpotifyAlbum::spotifyId)
                .orElseThrow();
        final var album = findAlbumBySpotifyIdOrCreate(spotifyAlbumId);
        final var track = trackCreator
                .toTrack(spotifyTrack)
                .withAlbum(album);
        album.addTrack(track);
        return album;
    }

    private Album findAlbumBySpotifyIdOrCreate(String spotifyAlbumId) {
        return albumRepository
                .findBySpotifyId(spotifyAlbumId)
                .orElseGet(() ->
                        albumCreator.createAlbum(spotifyQueries.getAlbumWithTracks(spotifyAlbumId))
                );
    }

}

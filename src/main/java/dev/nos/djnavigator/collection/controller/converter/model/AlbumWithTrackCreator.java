package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.repository.AlbumRepository;
import dev.nos.djnavigator.spotify.client.SpotifyQueries;
import org.springframework.stereotype.Service;

@Service
public class AlbumWithTrackCreator {
    private final TrackConverter trackConverter;
    private final AlbumConverter albumConverter;
    private final SpotifyQueries spotifyQueries;
    private final AlbumRepository albumRepository;

    public AlbumWithTrackCreator(TrackConverter trackConverter,
                                 AlbumConverter albumConverter,
                                 SpotifyQueries spotifyQueries,
                                 AlbumRepository albumRepository) {
        this.trackConverter = trackConverter;
        this.albumConverter = albumConverter;
        this.spotifyQueries = spotifyQueries;
        this.albumRepository = albumRepository;
    }

    public Album createAlbumWithTracks(String spotifyAlbumId) {
        final var album = findAlbumBySpotifyIdOrCreate(spotifyAlbumId);
        final var tracks = spotifyQueries
                .tracksWithAudioFeatures(spotifyAlbumId)
                .stream()
                .map(spotifyTrack -> trackConverter.createTrack(spotifyTrack, album))
                .toList();
        album.addTracks(tracks);
        return album;
    }

    public Album createAlbumWithTrack(String spotifyTrackId) {
        final var spotifyTrack = spotifyQueries.trackWithAudioFeature(spotifyTrackId);
        final var album = findAlbumBySpotifyIdOrCreate(spotifyTrack.spotifyAlbumId());
        final var track = trackConverter.createTrack(spotifyTrack, album);
        album.addTrack(track);
        return album;
    }

    private Album findAlbumBySpotifyIdOrCreate(String spotifyAlbumId) {
        return albumRepository
                .findBySpotifyId(spotifyAlbumId)
                .orElseGet(() ->
                        albumConverter.toAlbum(
                                spotifyQueries.albumWithTracks(spotifyAlbumId)
                        )
                );
    }

}

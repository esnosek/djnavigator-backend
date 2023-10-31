package dev.nos.djnavigator.collection.controller.converter.model;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId;
import dev.nos.djnavigator.collection.model.id.TrackSpotifyId;
import dev.nos.djnavigator.collection.repository.AlbumRepository;
import dev.nos.djnavigator.spotify.client.SpotifyQueries;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import org.springframework.stereotype.Service;

import static dev.nos.djnavigator.collection.controller.converter.model.ConvertersUtils.*;

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

    public Album createAlbumWithTracks(AlbumSpotifyId albumSpotifyId) {
        final var album = findAlbumBySpotifyIdOrCreate(toSpotifyAlbumId(albumSpotifyId));
        final var tracks = spotifyQueries
                .tracksWithAudioFeatures(toSpotifyAlbumId(albumSpotifyId))
                .stream()
                .map(spotifyTrack -> trackConverter.createTrack(spotifyTrack, album))
                .toList();
        album.addTracks(tracks);
        return album;
    }

    public Album createAlbumWithTrack(TrackSpotifyId trackSpotifyId) {
        final var spotifyTrack = spotifyQueries.trackWithAudioFeature(toSpotifyTrackId(trackSpotifyId));
        final var album = findAlbumBySpotifyIdOrCreate(spotifyTrack.spotifyAlbumId());
        final var track = trackConverter.createTrack(spotifyTrack, album);
        album.addTrack(track);
        return album;
    }

    private Album findAlbumBySpotifyIdOrCreate(SpotifyAlbumId spotifyAlbumId) {
        return albumRepository
                .findBySpotifyId(toAlbumSpotifyId(spotifyAlbumId))
                .orElseGet(() ->
                        albumConverter.toAlbum(
                                spotifyQueries.albumWithTracks(spotifyAlbumId)
                        )
                );
    }
}

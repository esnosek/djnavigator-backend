package dev.nos.djnavigator.collection.service;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.converter.AlbumFromSpotifyAlbumConverter;
import dev.nos.djnavigator.collection.model.converter.TrackFromSpotifyTrackConverter;
import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId;
import dev.nos.djnavigator.collection.repository.AlbumQueries;
import dev.nos.djnavigator.collection.repository.AlbumService;
import dev.nos.djnavigator.spotify.client.SpotifyQueries;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.nos.djnavigator.utils.ConvertersUtils.toAlbumSpotifyId;
import static dev.nos.djnavigator.utils.ConvertersUtils.toSpotifyAlbumId;

@Service
public class AlbumFromSpotifyCreator {

    private final TrackFromSpotifyTrackConverter spotifyTrackConverter;
    private final AlbumFromSpotifyAlbumConverter spotifyAlbumConverter;
    private final SpotifyQueries spotifyQueries;
    private final AlbumQueries albumQueries;
    private final AlbumService albumService;

    @Autowired
    public AlbumFromSpotifyCreator(TrackFromSpotifyTrackConverter spotifyTrackConverter,
                                   AlbumFromSpotifyAlbumConverter spotifyAlbumConverter,
                                   SpotifyQueries spotifyQueries,
                                   AlbumQueries albumQueries,
                                   AlbumService albumService) {
        this.spotifyTrackConverter = spotifyTrackConverter;
        this.spotifyAlbumConverter = spotifyAlbumConverter;
        this.spotifyQueries = spotifyQueries;
        this.albumQueries = albumQueries;
        this.albumService = albumService;
    }

    public Album saveAlbumWithTracks(AlbumSpotifyId albumSpotifyId) {
        final var album = findAlbumBySpotifyIdOrCreate(toSpotifyAlbumId(albumSpotifyId));
        final var tracks = spotifyQueries
                .tracksWithAudioFeatures(toSpotifyAlbumId(albumSpotifyId))
                .stream()
                .map(spotifyTrack -> spotifyTrackConverter.apply(spotifyTrack, album))
                .toList();
        album.addTracks(tracks);
        return albumService.save(album);
    }

    public Album findAlbumBySpotifyIdOrCreate(SpotifyAlbumId spotifyAlbumId) {
        return albumQueries
                .findBySpotifyId(toAlbumSpotifyId(spotifyAlbumId))
                .orElseGet(() ->
                        spotifyAlbumConverter.apply(
                                spotifyQueries.albumWithTracks(spotifyAlbumId)
                        )
                );
    }
}

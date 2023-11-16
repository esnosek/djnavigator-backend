package dev.nos.djnavigator.collection.service;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.collection.model.converter.AlbumFromSpotifyAlbumConverter;
import dev.nos.djnavigator.collection.model.converter.TrackFromSpotifyTrackConverter;
import dev.nos.djnavigator.collection.model.id.TrackSpotifyId;
import dev.nos.djnavigator.collection.repository.AlbumQueries;
import dev.nos.djnavigator.collection.repository.AlbumService;
import dev.nos.djnavigator.collection.repository.TrackService;
import dev.nos.djnavigator.spotify.client.SpotifyQueries;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.nos.djnavigator.utils.ConvertersUtils.toAlbumSpotifyId;
import static dev.nos.djnavigator.utils.ConvertersUtils.toSpotifyTrackId;

@Service
public class TrackFromSpotifyCreator {

    private final AlbumService albumService;
    private final TrackService trackService;
    private final AlbumFromSpotifyAlbumConverter spotifyAlbumConverter;
    private final TrackFromSpotifyTrackConverter spotifyTrackConverter;
    private final SpotifyQueries spotifyQueries;
    private final AlbumQueries albumQueries;

    @Autowired
    public TrackFromSpotifyCreator(AlbumService albumService,
                                   AlbumFromSpotifyAlbumConverter spotifyAlbumConverter,
                                   TrackFromSpotifyTrackConverter spotifyTrackConverter,
                                   TrackService trackService,
                                   SpotifyQueries spotifyQueries,
                                   AlbumQueries albumQueries) {
        this.albumService = albumService;
        this.spotifyAlbumConverter = spotifyAlbumConverter;
        this.spotifyTrackConverter = spotifyTrackConverter;
        this.trackService = trackService;
        this.spotifyQueries = spotifyQueries;
        this.albumQueries = albumQueries;
    }

    public Track saveTrackWithAlbum(TrackSpotifyId trackSpotifyId) {
        final var spotifyTrack = spotifyQueries.trackWithAudioFeature(toSpotifyTrackId(trackSpotifyId));
        final var album = albumQueries
                .findBySpotifyId(toAlbumSpotifyId(spotifyTrack.spotifyAlbumId()))
                .orElseGet(() -> createAndSave(spotifyTrack.spotifyAlbumId()));
        final var track = spotifyTrackConverter.apply(spotifyTrack, album);
        return trackService.save(track);
    }

    private Album createAndSave(SpotifyAlbumId spotifyAlbumId) {
        final var spotifyAlbum = spotifyQueries.albumWithTracks(spotifyAlbumId);
        final var album = spotifyAlbumConverter.apply(spotifyAlbum);
        return albumService.save(album);
    }

}
package dev.nos.djnavigator.spotify.client;

import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifyPlaylist;
import dev.nos.djnavigator.spotify.model.SpotifySearchResults;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotifyQueries {

    private final SpotifyApi spotifyApi;

    @Autowired
    SpotifyQueries(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    public SpotifyTrack trackWithAudioFeature(String spotifyTrackId) {
        final var spotifyTrack = spotifyApi.track(spotifyTrackId);
        final var audioFeatures = spotifyApi.audioFeatures(spotifyTrackId);
        return spotifyTrack.withAudioFeatures(audioFeatures);
    }

    public List<SpotifyTrack> tracksWithAudioFeatures(String spotifyAlbumId) {
        final var spotifyAlbum = spotifyApi.album(spotifyAlbumId);
        return withAudioFeatures(spotifyAlbum.spotifyTracks());
    }

    public SpotifyAlbum albumWithTracksAndAudioFeatures(String spotifyAlbumId) {
        final var spotifyAlbum = spotifyApi.album(spotifyAlbumId);
        final var spotifyTracks = withAudioFeatures(spotifyAlbum.spotifyTracks());
        return spotifyAlbum.withSpotifyTracks(spotifyTracks);
    }

    public SpotifySearchResults searchAlbumOrTrack(String query, int limit) {
        return spotifyApi.searchAlbumOrTrack(query, limit);
    }

    public SpotifyAlbum albumWithTracks(String albumId) {
        return spotifyApi.album(albumId);
    }

    public SpotifyPlaylist playlist(String playlistId) {
        return spotifyApi.playlist(playlistId);
    }


    private List<SpotifyTrack> withAudioFeatures(List<SpotifyTrack> spotifyTracks) {
        final var audioFeatures = spotifyApi.audioFeatures(spotifyIds(spotifyTracks));
        return spotifyTracks.stream()
                .map(track -> track.withAudioFeatures(audioFeatures.get(track.spotifyId())))
                .toList();
    }

    private static List<String> spotifyIds(List<SpotifyTrack> spotifyTracks) {
        return spotifyTracks
                .stream()
                .map(SpotifyTrack::spotifyId)
                .toList();
    }
}

package dev.nos.djnavigator.spotify;

import dev.nos.djnavigator.spotify.client.SpotifyQueries;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotifyTrackResolver {

    private final SpotifyQueries spotifyQueries;

    @Autowired
    public SpotifyTrackResolver(SpotifyQueries spotifyQueries) {
        this.spotifyQueries = spotifyQueries;
    }

    public SpotifyTrack trackWithAudioFeature(String spotifyTrackId) {
        final var spotifyTrack = spotifyQueries.getTrackWithAlbum(spotifyTrackId);
        final var audioFeatures = spotifyQueries.getAudioFeatureFor(spotifyTrackId);
        return spotifyTrack.withAudioFeatures(audioFeatures);
    }

    public List<SpotifyTrack> tracksWithAudioFeatures(String spotifyAlbumId) {
        final var spotifyAlbum = spotifyQueries.getAlbumWithTracks(spotifyAlbumId);
        return withAudioFeatures(spotifyAlbum.spotifyTracks().orElseThrow());
    }

    public SpotifyAlbum albumWithTracksAndAudioFeatures(String spotifyAlbumId) {
        final var spotifyAlbum = spotifyQueries.getAlbumWithTracks(spotifyAlbumId);
        final var spotifyTracks = withAudioFeatures(spotifyAlbum.spotifyTracks().orElseThrow());
        return spotifyAlbum.withSpotifyTracks(spotifyTracks);
    }

    private List<SpotifyTrack> withAudioFeatures(List<SpotifyTrack> spotifyTracks) {
        final var audioFeatures = spotifyQueries
                .getAudioFeaturesFor(
                        spotifyTracks
                                .stream()
                                .map(SpotifyTrack::spotifyId)
                                .toList()
                );
        return spotifyTracks.stream()
                .map(track -> track.withAudioFeatures(audioFeatures.get(track.spotifyId())))
                .toList();
    }
}

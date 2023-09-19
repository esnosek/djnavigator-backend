package dev.nos.djnavigator.spotify;

import dev.nos.djnavigator.spotify.client.SpotifyQueries;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifyPlaylist;
import dev.nos.djnavigator.spotify.model.SpotifySearchResults;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spotify")
class SpotifyController {

    private final SpotifyQueries spotifyQueries;
    private final SpotifyTrackResolver spotifyTrackResolver;

    @Autowired
    public SpotifyController(SpotifyQueries spotifyQueries,
                             SpotifyTrackResolver spotifyTrackResolver) {
        this.spotifyQueries = spotifyQueries;
        this.spotifyTrackResolver = spotifyTrackResolver;
    }

    @GetMapping("/search")
    public SpotifySearchResults search(@RequestParam String query, @RequestParam int limit) {
        return spotifyQueries.searchAlbumOrTrack(query, limit);
    }

    @GetMapping("/tracks/{spotifyTrackId}")
    public SpotifyTrack getSpotifyTrack(@PathVariable String spotifyTrackId) {
        return spotifyTrackResolver.trackWithAudioFeature(spotifyTrackId);
    }

    @GetMapping("/albums/{albumId}")
    public SpotifyAlbum getSpotifyAlbum(@PathVariable String albumId) {
        return spotifyTrackResolver.albumWithTracksAndAudioFeatures(albumId);
    }

    @GetMapping("/playlists/{playlistId}")
    public SpotifyPlaylist getSpotifyPlaylist(@PathVariable String playlistId) {
        return spotifyQueries.getPlaylist(playlistId);
    }
}

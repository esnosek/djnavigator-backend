package dev.nos.djnavigator.spotify;

import dev.nos.djnavigator.spotify.client.SpotifyQueriesAsync;
import dev.nos.djnavigator.spotify.model.SpotifyAlbum;
import dev.nos.djnavigator.spotify.model.SpotifyPlaylist;
import dev.nos.djnavigator.spotify.model.SpotifySearchResults;
import dev.nos.djnavigator.spotify.model.SpotifyTrack;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import dev.nos.djnavigator.spotify.model.id.SpotifyPlaylistId;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/spotify")
class SpotifyController {

    private final SpotifyQueriesAsync spotifyQueries;

    @Autowired
    public SpotifyController(SpotifyQueriesAsync spotifyQueries) {
        this.spotifyQueries = spotifyQueries;
    }

    @GetMapping("/search")
    public SpotifySearchResults search(@RequestParam String query, @RequestParam int limit) throws ExecutionException, InterruptedException {
        return spotifyQueries.searchAlbumOrTrack(query, limit)
                .get();
    }

    @GetMapping("/tracks/{spotifyTrackId}")
    public SpotifyTrack getSpotifyTrack(@PathVariable SpotifyTrackId spotifyTrackId) throws ExecutionException, InterruptedException {
        return spotifyQueries.trackWithAudioFeature(spotifyTrackId)
                .get();
    }

    @GetMapping("/albums/{albumId}")
    public SpotifyAlbum getSpotifyAlbum(@PathVariable SpotifyAlbumId albumId) throws ExecutionException, InterruptedException {
        return spotifyQueries.albumWithTracksAndAudioFeatures(albumId)
                .get();
    }

    @GetMapping("/playlists/{playlistId}")
    public SpotifyPlaylist getSpotifyPlaylist(@PathVariable SpotifyPlaylistId playlistId) throws ExecutionException, InterruptedException {
        return spotifyQueries.playlist(playlistId)
                .get();
    }
}

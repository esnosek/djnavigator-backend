package dev.nos.djnavigator.spotify.model.id;

public class SpotifyPlaylistId extends SpotifyId {

    private SpotifyPlaylistId() {
        super();
    }

    private SpotifyPlaylistId(String value) {
        super(value);
    }

    public static SpotifyPlaylistId from(String value) {
        return new SpotifyPlaylistId(value);
    }

    public static SpotifyPlaylistId randomId() {
        return new SpotifyPlaylistId();
    }
}
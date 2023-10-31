package dev.nos.djnavigator.spotify.model.id;

public class SpotifyAlbumId extends SpotifyId {

    private SpotifyAlbumId() {
        super();
    }

    private SpotifyAlbumId(String value) {
        super(value);
    }

    public static SpotifyAlbumId from(String value) {
        return new SpotifyAlbumId(value);
    }

    public static SpotifyAlbumId randomId() {
        return new SpotifyAlbumId();
    }
}

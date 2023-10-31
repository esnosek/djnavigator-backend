package dev.nos.djnavigator.spotify.model.id;

public class SpotifyTrackId extends SpotifyId {

    private SpotifyTrackId() {
        super();
    }

    private SpotifyTrackId(String value) {
        super(value);
    }

    public static SpotifyTrackId from(String value) {
        return new SpotifyTrackId(value);
    }

    public static SpotifyTrackId randomId() {
        return new SpotifyTrackId();
    }
}

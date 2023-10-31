package dev.nos.djnavigator.collection.model.id;

public class TrackSpotifyId extends EntityId {

    private TrackSpotifyId() {
        super();
    }

    private TrackSpotifyId(String value) {
        super(value);
    }

    public static TrackSpotifyId from(String value) {
        return new TrackSpotifyId(value);
    }

    public static TrackSpotifyId randomId() {
        return new TrackSpotifyId();
    }
}

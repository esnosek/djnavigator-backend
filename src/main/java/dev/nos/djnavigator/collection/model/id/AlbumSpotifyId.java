package dev.nos.djnavigator.collection.model.id;

public class AlbumSpotifyId extends EntityId {

    private AlbumSpotifyId() {
        super();
    }

    private AlbumSpotifyId(String value) {
        super(value);
    }

    public static AlbumSpotifyId from(String value) {
        return new AlbumSpotifyId(value);
    }

    public static AlbumSpotifyId randomId() {
        return new AlbumSpotifyId();
    }


}

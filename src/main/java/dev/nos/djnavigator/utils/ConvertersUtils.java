package dev.nos.djnavigator.utils;

import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId;
import dev.nos.djnavigator.collection.model.id.TrackSpotifyId;
import dev.nos.djnavigator.spotify.model.id.SpotifyAlbumId;
import dev.nos.djnavigator.spotify.model.id.SpotifyTrackId;

public class ConvertersUtils {

    public static SpotifyAlbumId toSpotifyAlbumId(AlbumSpotifyId albumSpotifyId) {
        return SpotifyAlbumId.from(albumSpotifyId.id());
    }

    public static SpotifyTrackId toSpotifyTrackId(TrackSpotifyId trackSpotifyId) {
        return SpotifyTrackId.from(trackSpotifyId.id());
    }

    public static AlbumSpotifyId toAlbumSpotifyId(SpotifyAlbumId albumSpotifyId) {
        return AlbumSpotifyId.from(albumSpotifyId.id());
    }

    public static TrackSpotifyId toTrackSpotifyId(SpotifyTrackId trackSpotifyId) {
        return TrackSpotifyId.from(trackSpotifyId.id());
    }

}

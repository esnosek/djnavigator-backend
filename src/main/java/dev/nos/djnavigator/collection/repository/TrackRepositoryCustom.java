package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.model.AlbumId;
import dev.nos.djnavigator.collection.model.Track;

import java.util.List;

public interface TrackRepositoryCustom {
    List<Track> findByAlbumId(AlbumId albumId);

    Track findBySpotifyId(String spotifyId);
}

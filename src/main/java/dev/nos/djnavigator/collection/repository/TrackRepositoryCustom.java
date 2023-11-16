package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import dev.nos.djnavigator.collection.model.id.TrackSpotifyId;

import java.util.List;
import java.util.Optional;

public interface TrackRepositoryCustom {
    List<Track> findByAlbumId(AlbumId albumId);

    Optional<Track> findBySpotifyId(TrackSpotifyId spotifyId);
}

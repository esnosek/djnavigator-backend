package dev.nos.djnavigator.repository;

import dev.nos.djnavigator.model.Track;

import java.util.List;

public interface TrackRepositoryCustom {
    List<Track> findByAlbumId(String albumId);

    Track findBySpotifyId(String spotifyId);
}

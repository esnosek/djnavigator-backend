package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId;

import java.util.Optional;

public interface AlbumRepositoryCustom {

    Optional<Album> findBySpotifyId(AlbumSpotifyId spotifyId);
}


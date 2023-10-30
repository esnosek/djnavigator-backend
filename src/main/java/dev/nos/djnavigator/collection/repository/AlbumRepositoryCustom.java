package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.model.Album;

import java.util.Optional;

public interface AlbumRepositoryCustom {

    Optional<Album> findBySpotifyId(String spotifyId);
}


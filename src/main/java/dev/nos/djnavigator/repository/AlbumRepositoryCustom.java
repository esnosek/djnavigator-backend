package dev.nos.djnavigator.repository;

import dev.nos.djnavigator.model.Album;

import java.util.Optional;

public interface AlbumRepositoryCustom {

    Optional<Album> findBySpotifyId(String spotifyId);
}


package dev.nos.djnavigator.repository;

import dev.nos.djnavigator.model.Album;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class AlbumRepositoryCustomImpl implements AlbumRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public AlbumRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Album> findBySpotifyId(String spotifyId) {
        final var session = entityManager.unwrap(Session.class);
        return session.createQuery("from Album a where a.spotifyId = :spotifyId", Album.class)
                .setParameter("spotifyId", spotifyId)
                .stream().findAny();
    }


}

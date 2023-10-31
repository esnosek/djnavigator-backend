package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.id.AlbumSpotifyId;
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
    public Optional<Album> findBySpotifyId(AlbumSpotifyId spotifyId) {
        final var session = entityManager.unwrap(Session.class);
        return session.createQuery("from Album a where a.spotifyId = :spotifyId", Album.class)
                .setParameter("spotifyId", spotifyId)
                .stream().findAny();
    }


}

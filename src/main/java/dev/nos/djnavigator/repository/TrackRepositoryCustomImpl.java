package dev.nos.djnavigator.repository;

import dev.nos.djnavigator.model.Track;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TrackRepositoryCustomImpl implements TrackRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public TrackRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Track> findByAlbumId(String albumId) {
        final var session = entityManager.unwrap(Session.class);
        return session.createQuery("from Track t where t.album.spotifyId = :albumId", Track.class)
                .setParameter("albumId", albumId)
                .list();
    }


    @Override
    public Track findBySpotifyId(String spotifyId) {
        final var session = entityManager.unwrap(Session.class);
        return session.createQuery("from Track t where t.spotifyId = :spotifyId", Track.class)
                .setParameter("spotifyId", spotifyId)
                .getSingleResult();
    }

}

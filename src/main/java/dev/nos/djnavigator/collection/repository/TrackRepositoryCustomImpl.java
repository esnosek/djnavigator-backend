package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.model.Track;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import dev.nos.djnavigator.collection.model.id.TrackSpotifyId;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class TrackRepositoryCustomImpl implements TrackRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public TrackRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Track> findByAlbumId(AlbumId albumId) {
        final var session = entityManager.unwrap(Session.class);
        return session.createQuery("from Track t where t.album.id = :albumId", Track.class)
                .setParameter("albumId", albumId)
                .list();
    }

    @Override
    public Optional<Track> findBySpotifyId(TrackSpotifyId spotifyId) {
        final var session = entityManager.unwrap(Session.class);
        return session.createQuery("from Track t where t.spotifyId = :spotifyId", Track.class)
                .setParameter("spotifyId", spotifyId)
                .stream().findAny();
    }

}

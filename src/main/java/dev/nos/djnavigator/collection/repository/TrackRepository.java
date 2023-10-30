package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.model.Track;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends CrudRepository<Track, String>, TrackRepositoryCustom {
}

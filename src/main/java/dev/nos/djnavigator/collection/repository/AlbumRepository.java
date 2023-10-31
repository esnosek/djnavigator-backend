package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.model.Album;
import dev.nos.djnavigator.collection.model.id.AlbumId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends CrudRepository<Album, AlbumId>, AlbumRepositoryCustom {
}

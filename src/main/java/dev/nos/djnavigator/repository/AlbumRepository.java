package dev.nos.djnavigator.repository;

import dev.nos.djnavigator.model.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends CrudRepository<Album, String>, AlbumRepositoryCustom {
}

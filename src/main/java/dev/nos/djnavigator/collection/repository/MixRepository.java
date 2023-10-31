package dev.nos.djnavigator.collection.repository;

import dev.nos.djnavigator.collection.model.Mix;
import dev.nos.djnavigator.collection.model.id.MixId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MixRepository extends CrudRepository<Mix, MixId> {
}
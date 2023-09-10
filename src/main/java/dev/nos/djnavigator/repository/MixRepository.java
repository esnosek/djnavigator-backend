package dev.nos.djnavigator.repository;

import dev.nos.djnavigator.model.Mix;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MixRepository extends CrudRepository<Mix, String> {
}
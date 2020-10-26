package main.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tag2PostRepository extends CrudRepository <Integer, Integer> {
}

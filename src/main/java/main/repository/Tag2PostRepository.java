package main.repository;

import main.entity.Tag;
import main.entity.Tag2Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tag2PostRepository extends JpaRepository<Tag2Post, Integer> {
    @Query(value = "SELECT t2p FROM Tag2Post t2p")
    List<Tag2Post> findAllTag2Posts();
}

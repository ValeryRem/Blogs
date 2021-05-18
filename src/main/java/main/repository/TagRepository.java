package main.repository;

import main.entity.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository <Tag, Integer>  {
    @Query(value = "SELECT t FROM Tag t")
    List<Tag> findAllTags();
}

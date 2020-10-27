package main.repository;

import main.entity.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository <Tag, Integer>  {
}

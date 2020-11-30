package main.repository;

import main.entity.Post;
import main.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer>{
}

package main.repository;

import main.entity.CaptchaCode;
import main.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Integer> {
}

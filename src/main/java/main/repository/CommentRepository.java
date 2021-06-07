package main.repository;

import main.entity.PostComment;
import main.entity.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    @Query("SELECT pc FROM PostComment pc WHERE pc.postId = ?1")
    Collection<PostComment> findAllPostCommentsByPostId (int postId);
}

package main.repository;

import main.entity.Post;
import main.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
  @Query ("SELECT p FROM Post p ORDER BY p.postId DESC")
  Page<Post> getRecentPosts (PageRequest pageRequest);

  @Query ("SELECT p FROM Post p ORDER BY SIZE(p.postComments) DESC")
  Page<Post> getPopularPosts(PageRequest pageRequest);

  @Query ("SELECT p FROM Post p ORDER BY SIZE(p.postLikes) DESC")
  Page<Post> getBestPosts(PageRequest pageRequest);

  @Query ("SELECT p FROM Post p ORDER BY p.timestamp")
  Page<Post> getEarlyPosts(PageRequest pageRequest);

  @Query("SELECT p FROM Post p WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED'")
  Collection<Post> findAllActivePosts ();

  @Query("SELECT p FROM Post p WHERE p.user = ?1")
  Collection<Post> findAllPostsByUser (User user);

}

package main.repository;

import main.entity.Post;
import main.requests.PostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
  @Query (value = "SELECT p FROM Post p ORDER BY post_id DESC")
  Page<Post> getRecentPosts (PageRequest pageRequest);

  @Query (value = "SELECT p FROM Post p JOIN p.postComments pc ORDER BY size(pc) DESC")
  Page<Post> getPopularPosts(PageRequest pageRequest);

  @Query (value = "SELECT p, COUNT(pv) AS cnt FROM Post p JOIN p.postVote pv WHERE pv == 1 ORDER BY cnt DESC")
  Page<Post> getBestPosts(PageRequest pageRequest);

  @Query (value = "SELECT p FROM Post p ORDER BY p.timestamp")
  Page<Post> getEarlyPosts(PageRequest pageRequest);

  @Query(value = "SELECT p FROM Post p WHERE p.isActive = 1")
  Collection<Post> findAllActivePosts ();
}

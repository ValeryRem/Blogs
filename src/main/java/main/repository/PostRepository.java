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
  @Query (value = "SELECT p FROM Post p ORDER BY p.postId DESC")
  Page<Post> getRecentPosts (PageRequest pageRequest);

  @Query (value = "SELECT p FROM Post p ORDER BY SIZE(p.postComments) DESC")
  Page<Post> getPopularPosts(PageRequest pageRequest);

  //SELECT a FROM Author a WHERE (SELECT count(b) FROM Book b WHERE a MEMBER OF b.authors ) > 1
  @Query (value = "SELECT p FROM Post p WHERE 1 MEMBER OF p.postVotes pvs ORDER BY count(SELECT pv FROM PostVote pv " +
          "WHERE pv MEMBER OF pvs AND pv = 1) DESC")
  Page<Post> getBestPosts(PageRequest pageRequest);

  @Query (value = "SELECT p FROM Post p ORDER BY p.timestamp")
  Page<Post> getEarlyPosts(PageRequest pageRequest);

  @Query(value = "SELECT p FROM Post p WHERE p.isActive = 1 AND p.moderationStatus = ModerationStatus.ACCEPTED")
  Collection<Post> findAllActivePosts ();

  @Query(value = "SELECT p FROM Post p WHERE p.user = ?1")
  Collection<Post> findAllPostsByUser (User user);
}

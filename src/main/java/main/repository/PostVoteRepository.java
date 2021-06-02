package main.repository;

import main.entity.Post;
import main.entity.PostVote;
import main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {
    @Query("SELECT pv FROM PostVote pv WHERE pv.userId = ?1")
    Collection<PostVote> findAllPostVotesByUserId (int userId);
}

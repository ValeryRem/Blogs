package main.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "post_votes")
public class PostVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer voteId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
//    @JsonProperty
    private Integer postId;
    private Timestamp time;
    private Integer value;

//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "post_id")
//    public Post post;

    public PostVote() {
    }

//    public Post getPost() {
//        return post;
//    }
//
//    public void setPost(Post post) {
//        this.post = post;
//    }

    public Integer getVoteId() {
        return voteId;
    }

    public void setVoteId(Integer voteId) {
        this.voteId = voteId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Timestamp getTime() {return time;}

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}

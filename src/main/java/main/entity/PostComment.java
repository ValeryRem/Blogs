package main.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TreeMap;

@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="comment_id")
    private Integer commentId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "post_id", nullable = false)
    @JsonProperty
    private Integer postId;

    @Column(name = "user_id")
    private Integer userId;
    private Timestamp time;
    private String text;

    public PostComment() {
//        userId = new Post(postId).getUserId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostComment )) return false;
        return commentId != null && commentId.equals(((PostComment) o).getCommentId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer id) {
        this.commentId = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

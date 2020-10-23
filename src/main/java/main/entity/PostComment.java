package main.entity;

import javax.persistence.*;
import java.time.LocalDate;

//@Embeddable
@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Integer commentId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
    private Integer postId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;
    private LocalDate time;
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public PostComment() {
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

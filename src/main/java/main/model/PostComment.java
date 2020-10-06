package main.model;

import org.springframework.web.bind.annotation.Mapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
//@Embeddable
@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "post_id", nullable = false, insertable = false, updatable = false)
    private Integer postId;
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @NotNull(message = "Time of the comment is mandatory")
    private Date time;
    @NotNull(message = "Text of the comment is mandatory")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostComment )) return false;
        return id != null && id.equals(((PostComment) o).getId());
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

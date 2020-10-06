package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "post_votes")
public class PostVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Id of the vote sender is mandatory")
    @Column(name = "user_id")
    private Integer userId;
    @NotNull(message = "Post_id is mandatory")
    @Column(name = "post_id")
    private Integer postId;
    @NotNull(message = "Time of the vote is mandatory")
    private Date time;
    @NotNull(message = "Value of the vote is mandatory")
    private Integer value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPost_id() {
        return postId;
    }

    public void setPost_id(Integer post_id) {
        this.postId = post_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}

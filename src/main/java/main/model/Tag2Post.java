package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tag_2_post")
public class Tag2Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Value of postId is mandatory")
    @Column(name = "post_id")
    private Integer postId;

    @NotNull(message = "Value of tagId is mandatory")
    @Column(name = "tag_id")
    private Integer tagId;

    public Tag2Post(Integer id, Integer postId, Integer tagId) {
        this.id = id;
        this.postId = postId;
        this.tagId = tagId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}

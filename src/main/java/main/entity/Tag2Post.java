package main.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag_2_post")
public class Tag2Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "tag_id")
    private Integer tagId;

    public Tag2Post() {
    }

    public Tag2Post(Integer postId, Integer tagId) {
        this.postId = postId;
        this.tagId = tagId;
    }

    public Integer getTag2postId() {
        return id;
    }

    public void setTag2postId(Integer id) {
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

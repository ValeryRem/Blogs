package main.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag_2_post")
public class Tag2Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_2_post_id")
    private Integer tag2postId;

    @Column(name = "post_id")
//    @OneToMany(mappedBy = "id")
    private Integer postId;

//    @OneToMany(mappedBy = "Id")
    @Column(name = "tag_id")
    private Integer tagId;

    public Tag2Post() {
    }

    public Tag2Post(Integer id, Integer postId, Integer tagId) {
        this.tag2postId = id;
        this.postId = postId;
        this.tagId = tagId;
    }

    public Integer getTag2postId() {
        return tag2postId;
    }

    public void setTag2postId(Integer tag2postId) {
        this.tag2postId = tag2postId;
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

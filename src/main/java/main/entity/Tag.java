package main.entity;

import javax.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tag_name")
    private String tagName;

    public Tag() {
    }

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        if(tagName.matches("#\\S+")) {
            this.tagName = tagName;
        } else {
            this.tagName = "";
        }
    }
}

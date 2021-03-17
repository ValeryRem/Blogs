package main.requests;

import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

public class CommentRequest implements Serializable {
    Integer id;
    Integer parent_id;
    String text;

    public Integer getPostId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package main.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CommentRequest implements Serializable {
    @JsonProperty
    Integer post_id;

    @JsonProperty
    Integer parent_id;

    @JsonProperty
    String text;

    public Integer getPostId() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
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

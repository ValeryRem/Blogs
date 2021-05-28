package main.requests;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CommentRequest implements Serializable {
    Integer post_id;
    Integer parent_id;
    String text;

    public Integer getPostId() {
        return post_id;
    }

    public void setPostId(Integer postId) {
        this.post_id = postId;
    }

    public Integer getParentId() {
        return parent_id;
    }

    public void setParentId(Integer parentId) {
        this.parent_id = parentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

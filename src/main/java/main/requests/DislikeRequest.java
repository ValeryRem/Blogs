package main.requests;

import java.io.Serializable;

public class DislikeRequest implements Serializable {
    Integer post_id;

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }
}

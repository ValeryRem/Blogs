package main.requests;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class DislikeRequest implements Serializable {
    public Integer post_id;

    public Integer getPostId() {
        return post_id;
    }

    public void setPostId(Integer post_id) {
        this.post_id = post_id;
    }
}

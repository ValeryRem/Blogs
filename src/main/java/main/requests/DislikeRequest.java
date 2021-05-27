package main.requests;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class DislikeRequest implements Serializable {
    Integer postId;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }
}

package main.requests;

import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class DislikeRequest implements Serializable {
    Integer post_id;

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }
}

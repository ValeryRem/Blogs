package main.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PostModerationRequest implements Serializable {
    @JsonProperty("post_id")
    Integer postId;

    @JsonProperty
    String decision;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}

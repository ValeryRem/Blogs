package main.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PostModerationRequest implements Serializable {
    Integer id;
    String decision;

    public Integer getPost_id() {
        return id;
    }

    public void setPost_id(Integer post_id) {
        this.id = post_id;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}

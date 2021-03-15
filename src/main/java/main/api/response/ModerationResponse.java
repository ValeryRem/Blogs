package main.api.response;

public class ModerationResponse {
    private Integer post_id;
    private  String decision;


    public ModerationResponse() {
    }

    public ModerationResponse(Integer post_id, String decision) {
        this.post_id = post_id;
        this.decision = decision;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}

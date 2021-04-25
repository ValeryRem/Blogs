package main.response;

public class ModerationResponse {
    private Integer id;
    private  String decision;


    public ModerationResponse() {
    }

    public ModerationResponse(Integer id, String decision) {
        this.id = id;
        this.decision = decision;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}

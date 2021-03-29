package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentsResponse {
    @JsonProperty
    private Integer id;

    @JsonProperty
    private long timestamp;

    @JsonProperty
    private  String text;

    @JsonProperty
    private UserResponse users;

    public CommentsResponse() {
    }

    public CommentsResponse(Integer id, long timestamp, String text, UserResponse users) {
        this.id = id;
        this.timestamp = timestamp;
        this.text = text;
        this.users = users;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserResponse getUsers() {
        return users;
    }

    public void setUsers(UserResponse users) {
        this.users = users;
    }
}

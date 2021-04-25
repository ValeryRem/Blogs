package main.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class CommentsResponse {
    private Integer id;
    private Timestamp timestamp;
    private  String text;
    private UserResponse users;

    public CommentsResponse() {
    }

    public CommentsResponse(Integer id, Timestamp timestamp, String text, UserResponse users) {
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
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

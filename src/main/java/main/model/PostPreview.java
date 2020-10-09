package main.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Date;

public class PostPreview {
    private final Integer postId;
    private final User user;
    private final Date date;
    private final Timestamp timestamp;
    private final String postAnnounce;

    public PostPreview(Integer postId, User user, Date date, Timestamp timestamp, String postAnnounce) {
        this.postId = postId;
        this.user = user;
        this.date = date;
        this.timestamp = timestamp;
        this.postAnnounce = postAnnounce;
    }

    public Integer getPostId() {
        return postId;
    }

    public User getUser() {
        return user;
    }

    public Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    public Date getDate() {
        return new Date();
    }
    public String getPostAnnounce() {
        return postAnnounce;
    }
}

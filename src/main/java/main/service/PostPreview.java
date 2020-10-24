package main.service;

import main.entity.Post;
import main.entity.User;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

public class PostPreview {
    private Integer postId;
    private User user;
    private LocalDate date;
    private Timestamp timestamp;
    private String postAnnounce;

    @Autowired
    private PostRepository postRepository;
    public PostPreview() {
    }

    public PostPreview(Integer postId, User user, LocalDate date, Timestamp timestamp, String postAnnounce) {
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
        return  postAnnounce;
    }
}

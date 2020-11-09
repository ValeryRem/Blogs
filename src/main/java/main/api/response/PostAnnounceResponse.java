package main.api.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import main.entity.Post;
import main.entity.User;
import main.service.PostService;

import java.time.LocalDate;

public class PostAnnounceResponse {
    private Integer id;
    private LocalDate timestamp;
    private User user ;
    private String title;
    private String announce;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;

    public PostAnnounceResponse(Post post)
    {
        this.id = post.getPostId();
        this.timestamp = post.getTime();
        this.title = post.getTitle();
        this.announce = post.getAnnounce();
        this.likeCount = post.getLikeCount();
        this.dislikeCount = post.getDislikeCount();
        this.viewCount = post.getViewCount();
        this.user = new User(id);
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getAnnounce() {
        return announce;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Integer getDislikeCount() {
        return dislikeCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }
}

package main.response;

import main.entity.Post;

import java.util.TreeMap;

public class MyPostResponse {
    private final Integer id;
    private final long timestamp;

    private final String title;
    private final String announce;
    private Integer likeCount;
    private Integer dislikeCount;
    private final Integer viewCount;
    private TreeMap<String, UserResponse> user;

    public MyPostResponse(Post post) {
        this.id = post.getUserId();
        this.timestamp = post.getTimestamp().getTime()/1000;
        this.title = post.getTitle();
        this.announce = post.getAnnounce();
        this.viewCount = post.getViewCount();
    }

    public Integer getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TreeMap<String, UserResponse> getUser() {
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

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public void setUser(TreeMap<String, UserResponse> user) {
        this.user = user;
    }
}

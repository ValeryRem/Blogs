package main.response;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PostResponse {
    private Integer id;
    private long timestamp;
    private UserResponse userResponse;
    private  String title;
    private String text;
    private  String announce;
    private Integer likeCount;
    private Integer dislikeCount;
    private  Integer commentCount;
    private  Integer viewCount;
    private boolean active;
    private List<String> tags;
    private List<CommentsResponse> comments;

    public Integer getId() {
            return id;
        }

        public long getTimestamp() {
            return timestamp;
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

        public Integer getCommentCount() {
            return commentCount;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<CommentsResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentsResponse> comments) {
        this.comments = comments;
    }
}


package main.response;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PostResponse {
    @JsonProperty
    private Integer id;

    @JsonProperty
    private long timestamp;

    @JsonProperty
    private UserResponse userResponse;

    @JsonProperty
    private  String title;

    @JsonProperty
    private String text;

    @JsonProperty
    private  String announce;

    @JsonProperty
    private Integer likeCount;

    @JsonProperty
    private Integer dislikeCount;

    @JsonProperty
    private  Integer commentCount;

    @JsonProperty
    private  Integer viewCount;

    @JsonProperty
    private boolean active;

    @JsonProperty
    private List<String> tags;

    @JsonProperty
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


package main.api.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import main.entity.Post;

import java.util.TreeMap;

public class PostResponse {
    @JsonProperty
    private  Integer id;

    @JsonProperty
    private  long timestamp;

    @JsonProperty
    private UserResponse userResponse;
//        private final TreeMap<String, Object> user;
    @JsonProperty
    private  String title;

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


//    public PostResponse(Integer postId, long timestamp, String title, String announce,
//                        Integer commentCount, Integer viewCount, UserResponse userResponse) {
//        this.id = postId;
//        this.timestamp = timestamp;
//        this.title = title;
//        this.announce = announce;
//        this.commentCount = commentCount;
//        this.viewCount = viewCount;
//        this.userResponse = userResponse;
//    }

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
}


package main.api.response;

import java.util.TreeMap;

public class PostsForModerationResponse {
    private Integer post_id;
    private  long timestamp;
    private  String title;
    private  String announce;
    private Integer likeCount;
    private Integer dislikeCount;
    private  Integer commentCount;
    private  Integer viewCount;
    private  TreeMap<String, Object> user;

    public PostsForModerationResponse(Integer post_id, long timestamp, String title,
                                      String announce, Integer likeCount, Integer dislikeCount, Integer commentCount,
                                      Integer viewCount, TreeMap<String, Object> user) {
        this.post_id = post_id;
        this.timestamp = timestamp;
        this.title = title;
        this.announce = announce;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.user = user;

    }


    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public TreeMap<String, Object> getUser() {
        return user;
    }

    public void setUser(TreeMap<String, Object> user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}

package main.api.response;

import main.entity.Post;
import main.entity.User;

import java.time.LocalDate;
import java.util.TreeMap;

public class MyPostResponce {
    private Integer id;
    private LocalDate timestamp;

    private String title;
    private String announce;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;
    private TreeMap<String, Object> user;

//    private User usr;

    public MyPostResponce (Post post) {
        this.id = post.getPostId();;
        this.timestamp = post.getTime();
        this.title = post.getTitle();
        this.announce = post.getAnnounce();
        this.likeCount = post.getLikeCount();
        this.dislikeCount = post.getDislikeCount();
        this.viewCount = post.getViewCount();
        this.user = getUserMap(post);
    }

    private TreeMap<String, Object> getUserMap (Post post) {
        user = new TreeMap<>();
        User usr = new User(post.getUserId());
        user.put("id", post.getUserId());
        user.put("name", usr.getName());
        return user;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public TreeMap<String, Object> getUser() {
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

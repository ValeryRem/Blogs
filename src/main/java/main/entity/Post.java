package main.entity;

import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
//    @JoinTable(name = "users", joinColumns = @JoinColumn(name = "user_id"))
    private Integer postId;

    @Column(name ="is_active")
    private boolean isActive;

    @Column(name ="moderation_status")
    @Enumerated(EnumType.ORDINAL)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @Column(name ="moderator_id")
    private Integer moderatorId;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate time;

    @Column(name ="id_of_user")
    private Integer userId;

    @ManyToOne (fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn (name="user")
    private User user;

    private TreeMap<String, Object> userMap;
    private String title;
    private String text;

    @Column(name ="like_count")
    private Integer likeCount;

    @Column(name ="dislike_count")
    private Integer dislikeCount;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name ="view_count")
    private Integer viewCount;

    public Post() {
    }

    public Post(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public Post(String title) {
        this.title = title;
    }

    public Post(Integer postId) {
        this.postId = postId;
    }

    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private List<PostComment> comments = new ArrayList<>();

    public List<PostComment> addComment(PostComment comment) {
        comments.add(comment);
        return comments;
    }

    public List<PostComment> removeComment(PostComment comment) {
        comments.remove(comment);
        return comments;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public Integer getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(Integer moderatorId) {
        this.moderatorId = moderatorId;
    }

    public LocalDate  getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnnounce() {
        String announce = getText().replaceAll("[\\p{P}\\p{S}]", "");
        try {
            if (announce.length() <= 500) {
                announce = announce.substring(0, text.length() / 5); // В анонс выводим 20% текста поста, но не более 100 знаков
            } else {
                announce = announce.substring(0, 100);
            }
        } catch (NullPointerException ex) {
           announce = "No text of post!";
        }
        return announce;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
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

    public List<PostComment> getComments() {
        return comments;
    }

    public void setComments(List<PostComment> comments) {
        this.comments = comments;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public TreeMap<String, Object> getUserMap() {
        User us = new User(userId);
        userMap = new TreeMap<>();
        try {
            userMap.put("id", us.getUserId());
            userMap.put("name", us.getName());
        } catch (NullPointerException npe) {
            userMap = new TreeMap<>();
        }
        return userMap;
    }

    public void setUserMap(TreeMap<String, Object> userMap) {
        this.userMap = userMap;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public TreeMap<String, Object> getUserShort() {
//        TreeMap<String, Object> map = new TreeMap<>();
//        map.put("id", userId);
//        map.put("name", new User(getUserId()).getName());
//        return map;
//    }
}

package main.entity;

import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "post_id")
    private Integer postId;

    @Column(name ="is_active")
    private Integer isActive;

    @Column(name ="moderation_status")
    @Enumerated(EnumType.ORDINAL)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @Column(name ="moderator_id")
    private Integer moderatorId;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate time;

    @Column(name ="user_id")
    private Integer userId;

    private String title;
    private String text;

    @Column(name ="view_count")
    private Integer viewCount;

    @Column(name ="like_count")
    private Integer likeCount;

    @Column(name ="dislike_count")
    private Integer dislikeCount;

    public Post() {
    }

    public Post(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public Post(String title, Integer postId) {
        this.title = title;
        this.postId = postId;
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

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
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
        String announce;
        try {
             announce = getText();
            if (announce.length() <= 500) {
                announce = announce.substring(0, text.length() / 5); // В анонс выводим 20% текста поста, но не более 100 знаков
            } else {
                announce = announce.substring(0, 100);
                announce.replaceAll("<script>.*?</script>", "");
            }
        } catch (NullPointerException ex) {
            announce =  "No text of post!";
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
}

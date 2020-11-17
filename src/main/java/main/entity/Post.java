package main.entity;

import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    @JoinTable(name = "users", joinColumns = @JoinColumn(name = "user_id"))
    private Integer postId;

    @Column(name ="is_active")
    private boolean isActive;

    @Column(name ="moderation_status")
    @Enumerated(EnumType.STRING)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @Column(name ="moderator_id")
    private Integer moderatorId;

    @Column(name ="user_id")
    private Integer userId;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate time;
    private String title;
    private String text;

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

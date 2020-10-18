package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Integer id;

    @Column(name ="is_active")
    @NotEmpty(message = "isActive status is mandatory")
    private Integer isActive;

    @Column(name ="moderation_status")
    @NotEmpty(message = "moderation_status is mandatory")
    @Enumerated(EnumType.STRING)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @Column(name ="moderator_id")
    private Integer moderatorId;

    @NotEmpty(message = "time of post is mandatory")
    private String time;

    @Column(name ="user_id")
    @NotEmpty(message ="userId is mandatory")
    private Integer userId;

    @NotEmpty(message ="title is mandatory")
    private String title;

    @NotEmpty(message ="text is mandatory")
    private String text;

    @NotEmpty(message ="announce is mandatory")
    private String announce;

    @NotEmpty(message = "viewCount is mandatory")
    @Column(name ="view_count")
    private Integer viewCount;

    @NotEmpty(message = "likeCount is mandatory")
    @Column(name ="like_count")
    private Integer likeCount;

    @NotEmpty(message = "dislikeCount is mandatory")
    @Column(name ="dislike_count")
    private Integer dislikeCount;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Post(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public Post(String title, Integer id) {
        this.title = title;
        this.id = id;
    }

    @OneToOne(targetEntity=Post.class, mappedBy="id", fetch=FetchType.EAGER)
    private List<PostComment> comments = new ArrayList<>();

    public List<PostComment> addComment(PostComment comment) {
        comments.add(comment);
        return comments;
    }

    public List<PostComment> removeComment(PostComment comment) {
        comments.remove(comment);
        return comments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTime() {
        String date;
        try {
            date = dateFormat.format(time);
        } catch (
                DateTimeParseException e) {
            date = "";
        }
        return date;
    }

    public void setTime(String time) {
        String date;
        try {
            date = dateFormat.format(time);
        } catch (
                DateTimeParseException e) {
            date = "";
        }
        this.time = date;
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
        announce = getText();
        if (announce.length() <= 500) {
            announce = announce.substring(0, text.length() / 5); // В анонс выводим 20% текста поста, но не более 100 знаков
        } else {
            announce = announce.substring(0, 100);
        }
        return announce.replaceAll("<script>.*?</script>", "");
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
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

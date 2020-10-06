package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Integer id;

    @Column(name ="is_active")
    @NotNull(message = "isActive status is mandatory")
    private Integer isActive;

    @Column(name ="moderation_status")
    @NotNull(message = "moderation_status is mandatory")
    @Enumerated(EnumType.STRING)
//    @OneToOne (mappedBy ="id") //Caused by: org.hibernate.AnnotationException: @Column(s) not allowed on a @OneToOne property: main.model.Post.moderationStatus
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @Column(name ="moderator_id")
    private Integer moderatorId;

    @NotNull(message = "time of post is mandatory")
    private Date time;

    @Column(name ="user_id")
    @NotNull(message ="userId is mandatory")
    private Integer userId;

    @NotNull(message ="title is mandatory")
    private String title;

    @NotNull(message ="text is mandatory")
    private String text;

    @NotNull(message ="announce is mandatory")
    private String announce;

    @NotNull(message = "viewCount is mandatory")
    @Column(name ="view_count")
    private Integer viewCount;

    @NotNull(message = "likeCount is mandatory")
    @Column(name ="like_count")
    private Integer likeCount;

    @NotNull(message = "dislikeCount is mandatory")
    @Column(name ="dislike_count")
    private Integer dislikeCount;

    public Post(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public Post(String title, Integer id) {
        this.title = title;
        this.id = id;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

    public Post addComment(PostComment comment) {
        comments.add(comment);
        comment.setPost(this);
        return this;
    }

    public Post removeComment(PostComment comment) {
        comments.remove(comment);
        comment.setPost(null);
        return this;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
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
        String text = getText();
        text.replaceAll("<script>.*?</script>", "");
        return text.substring(0,  text.length()/5);
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

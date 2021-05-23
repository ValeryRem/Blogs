package main.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    @JoinTable(name = "users", joinColumns = @JoinColumn(name = "user_id"))
    private Integer postId;

    @Column(name ="is_active")
    private int isActive;

    @Column(name ="moderation_status")
    @Enumerated(EnumType.STRING)
    private ModerationStatus moderationStatus;

    @Column(name ="moderator_id")
    private Integer moderatorId;

    @Column(name ="user_id")
    private Integer userId;
    private Timestamp timestamp;
    private String title;
    private String text;

    @Column(name ="view_count")
    private Integer viewCount;

    @OneToMany (mappedBy="postId", fetch=FetchType.EAGER)
    private Collection<PostComment> postComments;

    @OneToMany (mappedBy="postId", fetch=FetchType.EAGER)
    private Collection<PostVote> postVotes;

    @ManyToOne (cascade = CascadeType.ALL)
    private User user;


    public Post() {
    }

    public Collection<PostVote> getPostVotes() {
        return postVotes;
    }

    public void setPostVotes(Collection<PostVote> postVotes) {
        this.postVotes = postVotes;
    }

    public Collection<PostComment> getPostComments() {
        return postComments;
    }

    public void setPostComment(Collection<PostComment> postComments) {
        this.postComments = postComments;
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

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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

    public int isActive() {
        return isActive;
    }

    public void setActive(int activityMode) {
        isActive = activityMode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

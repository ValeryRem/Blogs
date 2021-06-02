package main.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;
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
    Integer postId;

    @Column(name ="is_active")
    int isActive;

    @Column(name ="moderation_status")
    @Enumerated(EnumType.STRING)
    ModerationStatus moderationStatus;

    @Column(name ="moderator_id")
    Integer moderatorId;

    @Column(name ="user_id")
    Integer userId;
    Timestamp timestamp;
    String title;
    String text;

    @Column(name ="view_count")
    private Integer viewCount;

    @OneToMany (mappedBy="commentId", fetch=FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Collection<PostComment> postComments;

    @OneToMany (mappedBy="voteId", fetch=FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Collection<PostVote> postVotes;

    @OneToMany (mappedBy="voteId", fetch=FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA) // это позволяет без лишнего запроса получить количество
    @Where(clause = "value = 1") // в этой коллекции будут только лайки
    private Collection<PostVote> postLikes;

    @ManyToOne (cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
    private User user;

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

    public Collection<PostVote> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(Collection<PostVote> postLikes) {
        this.postLikes = postLikes;
    }

//    public Collection<PostVote> getPostVotes() {
//        return postVotes;
//    }
//
//    public void setPostVotes(Collection<PostVote> postVotes) {
//        this.postVotes = postVotes;
//    }

    public Collection<PostComment> getPostComments() {
        return postComments;
    }

    public void setPostComment(Collection<PostComment> postComments) {
        this.postComments = postComments;
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

    public void setPostComments(Collection<PostComment> postComments) {
        this.postComments = postComments;
    }

    public Collection<PostVote> getPostVotes() {
        return postVotes;
    }

    public void setPostVotes(Collection<PostVote> postVotes) {
        this.postVotes = postVotes;
    }
}

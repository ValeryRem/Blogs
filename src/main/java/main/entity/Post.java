package main.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;
import org.jsoup.Jsoup;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "posts")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer postId;

    @Column(name = "is_active")
    int isActive;

    @Column(name = "moderation_status")
    @Enumerated(EnumType.STRING)
    ModerationStatus moderationStatus;

    @Column(name = "moderator_id")
    Integer moderatorId;

    @Column(name = "user_id")
    Integer userId;

    Timestamp timestamp;
    String title;
    String text;

    @Column(name = "view_count")
    private Integer viewCount;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Collection<PostComment> postComments;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Collection<PostVote> postVotes;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Where(clause = "value = 1")
    private Collection<PostVote> postLikes;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Where(clause = "value = -1")
    private Collection<PostVote> postDislikes;

    public Post() { }

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

    public Collection<PostVote> getPostDislikes() {
        return postDislikes;
    }

    public void setPostDislikes(Collection<PostVote> postDislikes) {
        this.postDislikes = postDislikes;
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
        String postText = getText();
        if( postText == null){
            return "";
        }

        String announce = postText.replaceAll("<(.*?)>","" ).replaceAll("[\\p{P}\\p{S}]", "");
        announce = announce.substring(0, Math.min(150, announce.length())) + "...";
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

}

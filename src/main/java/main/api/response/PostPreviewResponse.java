package main.api.response;

import main.model.Post;
import main.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.Date;

@Component
public class PostPreviewResponse {
    @Autowired
    private Post post;
    private Timestamp stamp;
    private Date date;
    @Autowired
    private User user;

    public PostPreviewResponse(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public Timestamp getStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public Date getDate() {
        return new Date(stamp.getTime());
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return post.getId();
    }

    public String getTitle() {
        return post.getTitle();
    }

    public String getAnnounce() {
        return post.getAnnounce();
    }

    public int getLikeCount() {
        return post.getLikeCount();
    }

    public int getDislikeCount() {
        return post.getDislikeCount();
    }

    public int getCommentCount() {
        return post.getComments().size();
    }

    public int getViewCount() {
        return post.getViewCount();
    }
}

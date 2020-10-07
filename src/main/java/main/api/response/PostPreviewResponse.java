package main.api.response;

import main.model.Post;
import main.model.User;
import main.service.PostService;
import org.hibernate.dialect.PostgreSQL10Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.Date;

@Component
public class PostPreviewResponse {
    private Integer postId;
    private Timestamp stamp;
    private Date date;
    @Autowired
    private PostService postService;

//    public PostPreviewResponse(int postId) {
//        this.postId = postId;
//    }

    public Post getPost() {
        return postService.getPosts().get(postId);
    }

    public Timestamp getStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public Date getDate() {
        return new Date(stamp.getTime());
    }

    public Integer getUserId() {
        return getPost().getUserId();
    }

    public String userName(){
        return new User(getPost().getUserId()).getName();
    }

    public int getPostId() {
        return getPost().getId();
    }

    public String getTitle() {
        return getPost().getTitle();
    }

    public String getAnnounce() {
        return getPost().getAnnounce();
    }

    public int getLikeCount() {
        return getPost().getLikeCount();
    }

    public int getDislikeCount() {
        return getPost().getDislikeCount();
    }

    public int getCommentCount() {
        return getPost().getComments().size();
    }

    public int getViewCount() {
        return getPost().getViewCount();
    }
}

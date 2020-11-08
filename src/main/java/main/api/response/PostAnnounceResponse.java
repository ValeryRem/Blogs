package main.api.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import main.entity.Post;
import main.entity.User;
import main.service.PostService;

import java.time.LocalDate;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PostAnnounceResponse {
    private Integer id;
    private LocalDate timestamp;
    private User user ;
    private String title;
    private String announce;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;

    public PostAnnounceResponse(Post post)
    {
        this.id = post.getPostId();
        this.timestamp = post.getTime();
        this.title = post.getTitle();
        this.announce = post.getAnnounce();
        this.likeCount = post.getLikeCount();
        this.dislikeCount = post.getDislikeCount();
        this.viewCount = post.getViewCount();
        this.user = new User(id);
    }
}

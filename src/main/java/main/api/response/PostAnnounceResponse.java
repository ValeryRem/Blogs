package main.api.response;

import main.entity.Post;
import main.entity.User;

import java.time.LocalDate;

public class PostAnnounceResponse {
    private Integer id;
    private LocalDate timestamp;
    private User user ;
    private String title;
    private String announce;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;

    public PostAnnounceResponse(Integer id)
    //, LocalDate timestamp, String title, String announce,
//                                Integer likeCount, Integer dislikeCount, Integer viewCount)
    {
        this.id = id;
        Post post = new Post(id);
        this.timestamp = post.getTime();
        this.title = post.getTitle();
        this.announce = post.getAnnounce();
        this.likeCount = post.getLikeCount();
        this.dislikeCount = post.getDislikeCount();
        this.viewCount = post.getViewCount();
        this.user = new User(id);
    }
}

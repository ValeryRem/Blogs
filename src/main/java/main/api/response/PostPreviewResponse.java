package main.api.response;

import main.entity.Post;
import main.repository.PostRepository;
import main.service.PostPreview;
import main.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class PostPreviewResponse {
    private Integer postId;
    private User user;
    private LocalDate date;
    private Timestamp timestamp;
    private String postAnnounce;

    @Autowired
    private PostRepository postRepository;

    public PostPreviewResponse(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    {
        try {
            Post post = postRepository.findById(postId).get();
            postAnnounce = post.getAnnounce();
        } catch (NullPointerException ex) {
            postAnnounce = "No such post, no announce.";
        }
    }

    private final PostPreview postPreview = new PostPreview(postId, user, date, timestamp, postAnnounce);

    public ResponseEntity<?> getPostPreviewResponse() {
        return ResponseEntity.ok(postPreview);
    }
}


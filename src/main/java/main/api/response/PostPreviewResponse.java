package main.api.response;

import main.service.PostPreview;
import main.entity.User;
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
    private final PostPreview postPreview = new PostPreview(postId, user, date, timestamp, postAnnounce);

    public ResponseEntity<?> getPostPreviewResponse() {
        return ResponseEntity.ok(postPreview);
    }
}


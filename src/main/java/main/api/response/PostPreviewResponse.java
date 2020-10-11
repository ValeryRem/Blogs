package main.api.response;

import main.model.PostPreview;
import main.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.Date;

@Component
public class PostPreviewResponse {
    private Integer postId;
    private User user;
    private Date date;
    private Timestamp timestamp;
    private String postAnnounce;
    private final PostPreview postPreview = new PostPreview(postId, user, date, timestamp, postAnnounce);

    public ResponseEntity<?> getPostPreviewResponse() {
        return ResponseEntity.ok(postPreview);
    }
}


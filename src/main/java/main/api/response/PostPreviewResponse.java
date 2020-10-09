package main.api.response;

import main.model.PostPreview;
import main.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PostPreviewResponse {
    private Integer postId;
    private User user;
    private Date date;
    private Timestamp timestamp;
    private String postAnnounce;
//    @Autowired
    private PostPreview postPreview = new PostPreview(postId, user, date, timestamp, postAnnounce);

    public ResponseEntity<?> getPostPreviewResponse() {
        List<Object> list = new ArrayList<>();
        list.add(postPreview.getUser());
        list.add(postPreview.getPostId());
        list.add(postPreview.getTimestamp());
        list.add(postPreview.getDate());
        list.add(postPreview.getPostAnnounce());
             return ResponseEntity.ok(list);
    }
}


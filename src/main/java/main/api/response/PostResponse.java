package main.api.response;

import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PostResponse {
    @Autowired
    private PostService postService;

    public ResponseEntity<?> getPostById (Integer postId) {
        return postService.getPostById(postId);
    }

    public ResponseEntity<?> getPostBySearch (String query, Integer limit, Integer offset, String mode) {
        return postService.getPostsBySearch(query, limit, offset, mode);
    }

    public ResponseEntity<?> getPostByDate (LocalDate date, Integer offset, Integer limit, String mode) {
        return postService.getPostsByDate(date, offset, limit, mode);
    }

    public ResponseEntity<?> getPostByTag (String tagName, Integer offset, Integer limit, String mode){
        return postService.getPostsByTag(tagName, offset, limit, mode);
    }
}

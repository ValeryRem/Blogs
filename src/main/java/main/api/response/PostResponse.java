package main.api.response;

import main.model.Post;
import main.model.PostList;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PostResponse {
    @Autowired
    private PostService postService;

    public ResponseEntity<?> getPostById (Integer postId) {
        return postService.getPostById(postId);
    }

    public ResponseEntity<PostList> getPostBySearch (String query, Integer limit, Integer offset, String mode) {
        return postService.getPostBySearch(query, limit, offset, mode);
    }
}

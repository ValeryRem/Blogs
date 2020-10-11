package main.api.response;

import main.model.Post;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PostResponse {
    @Autowired
    private PostService postService;

    public ResponseEntity<Post> getPostById (Integer postId) {
        return postService.getPostById(postId);
    }
}

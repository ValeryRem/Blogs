package main.api.response;

import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PostsListResponse {

    @Autowired
    private PostService postService;

    public ResponseEntity<?> getPostListResponse(Integer offset, Integer limit, String mode) {
        return postService.getPosts(offset, limit, mode);
    }
}

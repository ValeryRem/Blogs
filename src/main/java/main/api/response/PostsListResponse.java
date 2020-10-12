package main.api.response;

import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PostsListResponse {

    @Autowired
    private PostService postService;

    public ResponseEntity<?> getPostListResponse(Integer offset, Integer limit, boolean mode, boolean recent,
                                                 boolean popular, boolean best, boolean early) {
        return postService.getPosts(offset, limit, mode, recent, popular, best, early);
    }
}

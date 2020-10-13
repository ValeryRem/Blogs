package main.controllers;

import main.api.response.PostResponse;
import main.api.response.PostsListResponse;
import main.model.Post;
import main.model.PostList;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiPostController {
    @Autowired
    private PostResponse postResponse;

    @Autowired
    private PostsListResponse postsListResponse;

    @GetMapping("/post")
    @ResponseBody
    private ResponseEntity<?> getPosts (@RequestParam(defaultValue = "0") Integer offset,
                                        @RequestParam(defaultValue = "5") Integer limit,
                                        @RequestParam(defaultValue = "true") boolean mode,
                                        @RequestParam(defaultValue = "true") boolean recent,
                                        @RequestParam(defaultValue = "true") boolean popular,
                                        @RequestParam(defaultValue = "true") boolean best,
                                        @RequestParam(defaultValue = "true") boolean early)
    {
        return postsListResponse.getPostListResponse(offset, limit, mode,  recent, popular, best, early);
    }

    @GetMapping("/post/{id}")
    private ResponseEntity<Post> getPostById (Integer postId) {
        return postResponse.getPostById(postId);
    }

    @GetMapping("/post/search")
    private ResponseEntity<PostList> getPostBySearch (String query, Integer limit) {
        return postResponse.getPostBySearch(query, limit);
    }
}

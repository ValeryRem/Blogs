package main.controllers;

import main.api.response.PostResponse;
import main.api.response.PostsListResponse;
import main.model.Post;
import main.model.PostList;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private ResponseEntity<?> getPosts (@RequestParam(defaultValue="0") Integer offset,
                                        @RequestParam(defaultValue="5") Integer limit,
                                        @RequestParam(defaultValue="recent") String mode){
        System.out.println("Method getPosts activated.");
        return postsListResponse.getPostListResponse(offset, limit, mode);
    }

    @GetMapping("/post/{id:\\d+}")
    private ResponseEntity<?> getPostById (@PathVariable("id") Integer postId) {
        System.out.println("Method getPostById activated. ID requested: " + postId);
        return postResponse.getPostById(postId);
    }

    @GetMapping("/post/search")
    private ResponseEntity<PostList> getPostBySearch (@RequestParam(required=false) String query, Integer limit,
                                                      Integer offset,  @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsBySearch activated. Query:" + query);
        return postResponse.getPostBySearch(query, limit, offset, mode);
    }
}

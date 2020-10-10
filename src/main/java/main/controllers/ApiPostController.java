package main.controllers;

import main.base.Storage;
import main.model.Post;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiPostController {
    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    private ResponseEntity<?> getPosts () {
        return postService.getPosts();
    }

    @GetMapping("/post/{id}")
    private ResponseEntity<Post> getPostById (Integer postId) {
        return postService.getPostById(postId);
    }
}

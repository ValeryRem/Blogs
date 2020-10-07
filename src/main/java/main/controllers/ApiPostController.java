package main.controllers;

import main.model.Post;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/post")
public class ApiPostController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    private ResponseEntity<?> getPosts(int offset, int limit) {
        Iterable<Post> posts = postService.getPostRepository().findAll();
        List<Post> result = new ArrayList<>();
        for (Post post : posts) {
            result.add(post);
        }
        if (offset + limit <= result.size()) {
            return ResponseEntity.ok(result.subList(offset, offset + limit));
        } else {
            return ResponseEntity.ok(result.subList(offset, result.size()));
        }
    }
}

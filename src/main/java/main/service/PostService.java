package main.service;

import main.api.response.PostPreviewResponse;
import main.api.response.PostResponse;
import main.api.response.PostsListResponse;
import main.model.Post;
import main.model.PostList;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class PostService {
    private Integer count;

    @Autowired
    private PostRepository postRepository;

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, Integer mode) {
        Iterable<Post> posts = postRepository.findAll();
        List<Post> result = new ArrayList<>();
        for (Post post : posts) {
            result.add(post);
        }
        if (mode == 1) {
            result.sort(Comparator.comparing(Post::getTime));
        } else {
            result.sort(Comparator.comparing(Post::getTime).reversed());
        }
        count = result.size();

        if (count == 0) {
                return new ResponseEntity<>("The list of posts is empty", HttpStatus.NO_CONTENT);
            }
        PostList postList;
        if (offset + limit <= count) {
            postList = new PostList(count, result.subList(offset, offset + limit));
        } else {
            postList = new PostList(count, result.subList(offset, count));
        }
        return ResponseEntity.ok(postList);
    }

    public ResponseEntity<Post> getPostById (Integer postId) {
        try {
            Post post = postRepository.findById(postId).get();
            return new ResponseEntity<>(post, HttpStatus.FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public Integer getCount() {
        return count;
    }
}

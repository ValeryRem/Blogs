package main.service;

import main.entity.ModerationStatus;
import main.entity.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class PostService {
    private Integer count;
    private List<Post> result;
    private Iterable<Post> posts;
    private ResponseEntity<?> responseEntity;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private PostRepository postRepository;

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        posts = postRepository.findAll();
        List<Post> result = new ArrayList<>();
        for (Post post : posts) {
            result.add(post);
        }
        return processPosts(offset, limit, result, mode);
    }

    public ResponseEntity<?> getPostById(Integer postId) {
        try {
            Post post = postRepository.findById(postId).get();
            return new ResponseEntity<>(post, HttpStatus.FOUND);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>("Post with ID = " + postId + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getPostsBySearch(String query, Integer offset, Integer limit, String mode) {
        result = new ArrayList<>();
        posts = postRepository.findAll();
        if (query == null) {
            responseEntity = getPosts(offset, limit, mode);
        } else {
            for (Post post : posts) {
                if (post.getText().contains(query) && post.getIsActive() == 1 && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                    result.add(post);
                }
            }
            responseEntity = processPosts(offset, limit, result, mode);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getPostsByDate(LocalDate time, Integer offset, Integer limit, String mode) {
        posts = postRepository.findAll();
        result = new ArrayList<>();
        for (Post post : posts) {
            if (post.getTime().equals(time) && post.getIsActive() == 1 &&
                    post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                result.add(post);
            }
        }
        return processPosts(offset, limit, result, mode);
    }

    public ResponseEntity<?> getPostsByTag(@RequestParam String tagName, Integer offset, Integer limit, String mode) {
        posts = postRepository.findAll();
        result = new ArrayList<>();
        if (tagName.matches("#\\S+")) {
            try {
                for (Post post : posts) {
                    if (post.getText().contains(tagName) && post.getIsActive() == 1 &&
                            post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                        result.add(post);
                    }
                }
                responseEntity = processPosts(offset, limit, result, mode);
            } catch (NullPointerException ex) {
                responseEntity = getPosts(offset, limit, mode);
            }
        } else {
            responseEntity = getPosts(offset, limit, mode);
        }
        return responseEntity;
    }

    private ResponseEntity<PostList> processPosts(int offset, Integer limit, List<Post> posts, String mode) {
        count = posts.size();
        PostList postList;
        switch (mode) {
            case "popular":
                posts.sort(Comparator.comparing(Post::getViewCount).reversed());
                break;
            case "best":
                posts.sort(Comparator.comparing(Post::getLikeCount).reversed());
                break;
            case "early":
                posts.sort(Comparator.comparing(Post::getTime));
                break;
            default:
                posts.sort(Comparator.comparing(Post::getTime).reversed());
        }

        if (count == 0) {
            postList = new PostList(count, posts);
            return new ResponseEntity<>(postList, HttpStatus.NOT_FOUND);
        }
        if (limit <= count) {
            postList = new PostList(count, posts.subList(offset, limit));
        } else {
            postList = new PostList(count, posts);
        }
        return ResponseEntity.ok(postList);
    }

    public Integer getCount() {
        return count;
    }
}

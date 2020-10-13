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
import java.util.Comparator;
import java.util.List;

@Service
public class PostService {
    private Integer count;

    @Autowired
    private PostRepository postRepository;
    private PostList postList;

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, boolean mode, boolean recent,
                                      boolean popular, boolean best, boolean early) {
        Iterable<Post> posts = postRepository.findAll();
        List<Post> result = new ArrayList<>();
        for (Post post : posts) {
            result.add(post);
        }
        if (mode) {
            if (recent) {
                result.sort(Comparator.comparing(Post::getTime).reversed());
            }
            if(popular){
                result.sort(Comparator.comparing(Post::getViewCount).reversed());
            }
            if(best){
                result.sort(Comparator.comparing(Post::getLikeCount).reversed());
            }
            if(early){
                result.sort(Comparator.comparing(Post::getTime));
            }
        }
        count = result.size();

        if (count == 0) {
            postList = new PostList(count, result);
            return new ResponseEntity<>(postList, HttpStatus.NO_CONTENT);
        }
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

    public ResponseEntity<PostList> getPostBySearch (String query, Integer limit) {
        Iterable<Post> posts = postRepository.findAll();
        List<Post> result = new ArrayList<>();
        for (Post post : posts) {
            if (post.getText().contains(query)) {
                result.add(post);
            }
        }
        count = result.size();
        if (count == 0) {
            postList = new PostList(count, result);
            return new ResponseEntity<>(postList, HttpStatus.NO_CONTENT);
        }
        if (limit <= count) {
            postList = new PostList(count, result.subList(0, limit));
        } else {
            postList = new PostList(count, result);
        }
        return ResponseEntity.ok(postList);
    }


    public Integer getCount() {
        return count;
    }
}

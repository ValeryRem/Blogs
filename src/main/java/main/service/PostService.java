package main.service;

import main.base.Storage;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostComment;
import main.model.PostList;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PostService {
    private Integer count;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private PostRepository postRepository;

    {
        Post post = new Post("The test post", 1);
        post.setAnnounce("Testing post");
        PostComment comment1 = new PostComment();
        PostComment comment2 = new PostComment();
        comment1.setText("Comment 1");
        comment2.setText("Comment 2");
        List<PostComment> listOfComments = Arrays.asList(comment1, comment2);
        post.setComments(listOfComments);
        post.setDislikeCount(5);
        post.setId(1);
        post.setIsActive(1);
        post.setLikeCount(10);
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        post.setText("This is a testing text");
        post.setTime("2020-10-18");
        post.setUserId(22);
        post.setViewCount(111);
        new Storage().addPost(post);
    }

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        Iterable<Post> posts = postRepository.findAll();
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
        List<Post> result = new ArrayList<>();
        Iterable<Post> posts = postRepository.findAll();
        ResponseEntity<?> responseEntity;
        if (query == null) {
            responseEntity = getPosts(offset, limit, mode);
        } else {
            for (Post post : posts) {
                if (post.getText().contains(query) && post.getIsActive() == 1 && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                    result.add(post);
                }
            }
            responseEntity = processPosts(limit, offset, result, mode);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getPostsByDate(String time, Integer offset, Integer limit, String mode) {
        Iterable<Post> posts = postRepository.findAll();
        List<Post> result = new ArrayList<>();
        ResponseEntity<?> responseEntity;
        String date;
        try {
            date = dateFormat.format(time);
        } catch (Exception e) {
            date = "";
        }
        if (date.equals("")) {
            responseEntity = getPosts(offset, limit, mode);
        } else {
            for (Post post : posts) {
                if (post.getTime().equals(date) && post.getIsActive() == 1 &&
                        post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                    result.add(post);
                }
            }
            responseEntity = processPosts(limit, offset, result, mode);
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

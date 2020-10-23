package main.controllers;

import main.api.response.PostResponse;
import main.api.response.PostsListResponse;
import main.base.Storage;
import main.entity.ModerationStatus;
import main.entity.Post;
import main.entity.PostComment;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiPostController {
    @Autowired
    private PostResponse postResponse;

    @Autowired
    private PostsListResponse postsListResponse;

    @Autowired
    private static Storage storage;

    @GetMapping("/post")
    @ResponseBody
    private ResponseEntity<?> getPosts (@RequestParam(defaultValue="0") Integer offset,
                                        @RequestParam(defaultValue="5") Integer limit,
                                        @RequestParam(defaultValue="recent") String mode){
        try{
            setTestPost(storage);
        }
        catch (NullPointerException ex) {
            System.out.println("Storage is not initialized!");
        }
        System.out.println("Method getPosts activated. Number of posts: " + new PostService().getCount());
        return postsListResponse.getPostListResponse(offset, limit, mode);
    }

    @GetMapping("/post/{id:\\d+}")
    private ResponseEntity<?> getPostById (@PathVariable("id") Integer postId) {
        System.out.println("Method getPostById activated. ID requested: " + postId);
        return postResponse.getPostById(postId);
    }

    @GetMapping("/post/search")
    private ResponseEntity<?> getPostBySearch (@RequestParam(required=false) String query,
                                               @RequestParam(defaultValue="0") Integer offset,
                                               @RequestParam(defaultValue="5") Integer limit,
                                               @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsBySearch activated. Query:" + query);
        return postResponse.getPostBySearch(query, limit, offset, mode);
    }

    @GetMapping("/post/byDate")
    private ResponseEntity<?> getPostByDate (@DateTimeFormat(pattern = "YYYY-MM-dd") LocalDate date,
                                             @RequestParam(defaultValue="0") Integer offset,
                                             @RequestParam(defaultValue="5")Integer limit,
                                             @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByDate activated. Date:" + date );
        return postResponse.getPostByDate(date, offset, limit, mode);
    }

    @GetMapping("/post/byTag")
    private ResponseEntity<?> getPostsByTag(String tagName,
                                            @RequestParam(defaultValue="0") Integer offset,
                                            @RequestParam(defaultValue="5")Integer limit,
                                            @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByTag used. Tag:" + tagName );
        return postResponse.getPostByTag(tagName, offset, limit, mode);
    }

    private void setTestPost(Storage storage) {
        Post post = new Post("The test post #1", 1);
        PostComment comment1 = new PostComment();
        PostComment comment2 = new PostComment();
        comment1.setText("Comment 1");
        comment2.setText("Comment 2");
        List<PostComment> listOfComments = Arrays.asList(comment1, comment2);
        post.setComments(listOfComments);
        post.setDislikeCount(5);
        post.setPostId(1);
        post.setIsActive(1);
        post.setLikeCount(10);
        post.setModerationStatus(ModerationStatus.NEW);
        post.setText("This is a testing text. #test");
        post.setTime(LocalDate.now());
        post.setUserId(22);
        post.setViewCount(111);
        storage.addPost(post);
    }
}

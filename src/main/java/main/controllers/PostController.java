package main.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import main.repository.PostRepository;
import main.repository.SessionRepository;
import main.repository.UserRepository;
import main.requests.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/post", produces = {MediaType.APPLICATION_JSON_VALUE})
public class PostController {
    private final GetService getService;
    private final PostService postService;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final SessionRepository sessionRepository;
    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final PostRequest postRequest;
    private int ID;

    public PostController(GetService getService, PostService postService, AuthService authService, PostRepository postRepository,
                          SessionRepository sessionRepository, HttpSession httpSession, UserRepository userRepository,
                          PostRequest postRequest) {
        this.getService = getService;
        this.postService = postService;
        this.authService = authService;
        this.postRepository = postRepository;
        this.sessionRepository = sessionRepository;
        this.httpSession = httpSession;
        this.userRepository = userRepository;
        this.postRequest = postRequest;
    }

    @GetMapping("")
    @ResponseBody
    private ResponseEntity<?> getPosts (@RequestParam(defaultValue="0") Integer offset,
                                        @RequestParam(defaultValue="10") Integer limit,
                                        @RequestParam(defaultValue="recent") String mode){
        System.out.println("Method getPosts activated. Number of posts: " + getService.getCount());
        return getService.getPosts (offset, limit, mode);
    }

    @GetMapping("/{ID:\\d+}")
    //@RequestMapping(value = "/{ID: \\d+}", produces = "application/json", method = RequestMethod.GET)
    private ResponseEntity<?> getPostById (@PathVariable("ID") Integer ID) {
        System.out.println("Method getPostById activated. ID requested: " + ID);
        return getService.getPostById(ID);
    }

    @GetMapping("/search")
    private ResponseEntity<?> getPostsBySearch (@RequestParam(defaultValue="0") Integer offset,
                                                @RequestParam(defaultValue="5") Integer limit,
                                                @RequestParam String query) {
        System.out.println("Method getPostsBySearch activated. Query:" + query);
        return getService.getPostsBySearch(offset, limit, query);
    }

    @GetMapping("/byDate")
    private ResponseEntity<?> getPostsByDate (@RequestParam(defaultValue="0") Integer offset,
                                                @RequestParam(defaultValue="5") Integer limit,
                                                @RequestParam  String date){
        System.out.println("Method getPostsByDate activated by the date: " + date );
        return getService.getPostsByDate(offset, limit, date);
    }

    @GetMapping("/byTag")
    private ResponseEntity<?> getPostsByTag(@RequestParam(defaultValue="0") Integer offset,
                                            @RequestParam(defaultValue="5") Integer limit,
                                            @RequestParam String tag){
        System.out.println("Method getPostsByTag uses tag name:" + tag);
        return getService.getPostsByTag(offset, limit, tag);
    }

    @GetMapping("/moderation")
    private ResponseEntity<?> getPostsForModeration(@RequestParam(defaultValue="0") Integer offset,
                                                    @RequestParam(defaultValue="3") Integer limit,
                                                    @RequestParam(defaultValue="NEW") String status) {
        System.out.println("Method getPostsForModeration activated.");
        return getService.getPostsForModeration(offset, limit, status);
    }

    @GetMapping("/my")
    private ResponseEntity<?> getMyPosts (@RequestParam(defaultValue="0") Integer offset,
                                          @RequestParam(defaultValue="5") Integer limit) {
        System.out.println("Method getMyPosts activated.");
        return getService.getMyPosts(offset, limit);
    }

    @PostMapping("/like")
    private ResponseEntity<?> postLike (@RequestParam Integer post_id){//@RequestBody LikeRequest likeRequest) {
        System.out.println("Method postLike activated");
        return postService.postLikeDislike(post_id, 1);
    }

    @PostMapping("/dislike")
    private ResponseEntity<?> postDislike (@RequestParam Integer post_id)//(@RequestBody LikeRequest likeRequest)
    {
        System.out.println("Method postDislike activated");
        return postService.postLikeDislike(post_id, 0);
    }

    @PostMapping("")
    private ResponseEntity<?> postPost (@RequestBody PostRequest postRequest) {
        System.out.println("Method postPost is activated");
        return postService.postPost(postRequest);
    }

    @PutMapping(value = "/{id:\\d+}")
    public ResponseEntity<?> putPost (@PathVariable(value = "id") int id, @RequestBody PutPostRequest putPostRequest){
        System.out.println("Method putPost is activated");
        return  postService.putPost(id, putPostRequest);
    }
}


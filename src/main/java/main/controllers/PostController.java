package main.controllers;

import main.repository.PostRepository;
import main.repository.SessionRepository;
import main.repository.UserRepository;
import main.requests.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private GetService getService;

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/post")
    @ResponseBody
    private ResponseEntity<?> getPosts (@RequestParam(defaultValue="0") Integer offset,
                                        @RequestParam(defaultValue="7") Integer limit,
                                        @RequestParam(defaultValue="recent") String mode){
        System.out.println("Method getPosts activated. Number of posts: " + getService.getCount());
        return getService.getPosts (offset, limit, mode);
    }

    @GetMapping("/post/{id:\\d+}")
    //@RequestMapping(value = "/post/{id: \\d+}", produces = "application/json", method = RequestMethod.GET)
    private ResponseEntity<?> getPostById (@PathVariable("id") Integer postId) {
        System.out.println("Method getPostById activated. ID requested: " + postId);
        return getService.getPostById(postId);
    }

    @GetMapping("/post/search/{query:\\s+}")
    private ResponseEntity<?> getPostsBySearch (
            @PathVariable("query") String query,
            @RequestParam(defaultValue="0") Integer offset,
            @RequestParam(defaultValue="4") Integer limit,
            @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsBySearch activated. Query:" + query);
        return getService.getPostsBySearch(query, offset, limit, mode);
    }

    @GetMapping("/post/byDate/{date}")
    private ResponseEntity<?> getPostsByDate (
            @PathVariable("date") LocalDateTime date,
            @RequestParam(defaultValue="0") Integer offset,
            @RequestParam(defaultValue="5") Integer limit,
            @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByDate activated. Date:" + date );
        return getService.getPostsByDate(date, offset, limit, mode);
    }

    @GetMapping("/post/byTag/{tag}")
    private ResponseEntity<?> getPostsByTag(@PathVariable("tag") String tag,
                                            @RequestParam(defaultValue="0") Integer offset,
                                            @RequestParam(defaultValue="1") Integer limit,
                                            @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByTag uses tag name:" + tag);
        return getService.getPostsByTag(tag, offset, limit, mode);
    }

    @GetMapping("/post/moderation")
    private ResponseEntity<?> getPostsForModeration(@RequestParam(defaultValue="0") Integer offset,
                                                    @RequestParam(defaultValue="3") Integer limit,
                                                    @RequestParam(defaultValue="new") String status) {
        System.out.println("Method getPostsForModeration activated.");
        return getService.getPostsForModeration(offset, limit, status);
    }

    @GetMapping("/post/my")
    private ResponseEntity<?> getMyPosts (
                                          @RequestParam(defaultValue="0") Integer offset,
                                          @RequestParam(defaultValue="5") Integer limit) {
        System.out.println("Method getMyPosts activated.");
        return getService.getMyPosts(offset, limit);
    }

    @PostMapping("/post/like")
    private ResponseEntity<?> postLike (@RequestBody LikeRequest likeRequest)
                                        {
        System.out.println("Method postLike activated");
        return postService.postLike(likeRequest.getPost_id());
    }

    @PostMapping("/post/dislike")
    private ResponseEntity<?> postDislike (@RequestBody DislikeRequest dislikeRequest)
    {
        System.out.println("Method postDislike activated");
        return postService.postDislike(dislikeRequest.getPost_id());
    }

    @PostMapping("/post")
    private ResponseEntity<?> postPost (@RequestBody PostRequest postRequest) {
        System.out.println("userId: " + authService.getUserId());
        System.out.println("Method postPost is activated");
        return postService.postPost(postRequest.getTimestamp(), postRequest.getActive(), postRequest.getTitle(),
                postRequest.getTags(), postRequest.getText());
    }

    @PutMapping("/post/{ID: \\d+}")
    private ResponseEntity<?> putPost (@RequestBody PutPostRequest putPostRequest
//            @PathVariable("ID") Integer ID,
//            @RequestParam Long timestamp, @RequestParam Integer active, @RequestParam String title,
//            @RequestParam List<String> tags, @RequestParam String text
    )
    {
        System.out.println("Method putPost is activated");
        System.out.println("controller: " + putPostRequest.getTitle()); // test
//        return postService.putPost(ID, timestamp, active, title, tags, text);
        return postService.putPost(putPostRequest.getTimestamp(), putPostRequest.getIsActive(), putPostRequest.getTitle(),
                putPostRequest.getTags(), putPostRequest.getText());
    }

    @PostMapping("/comment")
    private ResponseEntity<?> postComment (@RequestBody CommentRequest commentRequest){
        System.out.println("Method postComment is activated.");
        return postService.postComment(commentRequest.getParent_id(), commentRequest.getPostId(), commentRequest.getText());
    }
//    private void registerSession () {
//        Session session = new Session();
//        session.setSessionName(httpSession.getId());
//        long epochSeconds = Instant.now().getEpochSecond();
//        session.setTime(epochSeconds);
//        sessionRepository.save(session);
//        List<Session> oldSessions = sessionRepository.findAll().stream().
//                filter(s -> s.getTime() < epochSeconds - 1800).
//                collect(Collectors.toList());
//        for (Session s: oldSessions) {
//            sessionRepository.delete(s);
//        }
//    }

//    private Integer getUserId () {
//        Integer userId = sessionRepository.findAll().stream().
//                filter(s -> s.getSessionName().equals(httpSession.getId())).
//                map(Session::getUserId).
//                findAny().orElse(0);
//        return userId;
//    }
}


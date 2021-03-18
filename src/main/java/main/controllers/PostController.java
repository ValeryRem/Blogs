package main.controllers;

import main.repository.PostRepository;
import main.repository.SessionRepository;
import main.repository.UserRepository;
import main.requests.CommentRequest;
import main.requests.PostRequest;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
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
    private ResponseEntity<?> getPostById (@PathVariable("id") Integer postId) {
        System.out.println("Method getPostById activated. ID requested: " + postId);
        return getService.getPostById(postId);
    }

    @GetMapping("/post/search/")
    private ResponseEntity<?> getPostsBySearch (@RequestParam(defaultValue = "new testing") String query,
                                               @RequestParam(defaultValue="0") Integer offset,
                                               @RequestParam(defaultValue="4") Integer limit,
                                               @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsBySearch activated. Query:" + query);
        return getService.getPostsBySearch(query, offset, limit, mode);
    }

    @GetMapping("/post/byDate")
    private ResponseEntity<?> getPostsByDate (
            @RequestParam long date, @RequestParam(defaultValue="0") Integer offset, @RequestParam(defaultValue="5") Integer limit,
                                             @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByDate activated. Date:" + date );
        return getService.getPostsByDate(date, offset, limit, mode);
    }

    @GetMapping("/post/byTag")
    private ResponseEntity<?> getPostsByTag(@RequestParam(defaultValue = "PHP") String tagName,
                                            @RequestParam(defaultValue="0") Integer offset,
                                            @RequestParam(defaultValue="1") Integer limit,
                                            @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByTag uses tag name:" + tagName);
        return getService.getPostsByTag(tagName, offset, limit, mode);
    }

    @GetMapping("/post/moderation")
    private ResponseEntity<?> getPostsForModeration(@RequestParam(defaultValue="0") Integer offset,
                                                    @RequestParam(defaultValue="3") Integer limit,
                                                    @RequestParam(defaultValue="new") String status) {
        System.out.println("Method getPostsForModeration activated.");
        return getService.getPostsForModeration(offset, limit, status);
    }

    @GetMapping("/my")
    private ResponseEntity<?> getMyPosts (
                                          @RequestParam(defaultValue="0") Integer offset,
                                          @RequestParam(defaultValue="5") Integer limit) {
        System.out.println("Method getMyPosts activated.");
        return getService.getMyPosts(offset, limit);
    }

    @PostMapping("/post/like")
    private ResponseEntity<?> postLike (@RequestBody Integer id)
//                                        @RequestParam(defaultValue="2") Integer userId)
                                        {
        System.out.println("Method postLike activated");
        return postService.postLike(id);
    }

    @PostMapping("/post/dislike")
    private ResponseEntity<?> postDislike (@RequestParam(defaultValue="5") Integer postToLikeId,
                                           @RequestParam(defaultValue="2") Integer userId) {
        System.out.println("Method postDislike activated");
        return postService.postDislike(postToLikeId, userId);
    }

    @PostMapping("/post")
    private ResponseEntity<?> postPost (@RequestBody PostRequest postRequest) {
//            @RequestParam long timestamp,
//                                        @RequestParam(defaultValue="1") Integer active,
//                                        @RequestParam(defaultValue="Optional.class description.") String title,
//                                        @RequestParam(defaultValue="[Java, Python]") List<String> tags,
//                                        @RequestParam(defaultValue="Try to consider how to implement Optional class " +
//                                                "approach if behaviour of components is nullable.") String text)
        System.out.println("userId: " + authService.getUserId());
        System.out.println("Method postPost is activated");
        return postService.postPost(postRequest.getTimestamp(), postRequest.getActive(), postRequest.getTitle(),
                postRequest.getTags(), postRequest.getText());
    }

    @PutMapping("/post/{id:\\d+}")
    private ResponseEntity<?> putPost (@PathVariable("id") Integer postId,
                                       @RequestParam(defaultValue="1") Integer active,
                                       @RequestParam(defaultValue="Optional description.") String title,
                                       @RequestParam(defaultValue="Java, PHP, Excel, Darby, Python") List<String> tags,
                                       @RequestParam(defaultValue="Try to escape from here, and as soon as possible, my daring!") String text) {
        System.out.println("Method putPost is activated for postId: " + postId);
        return postService.putPost(postId, active, title, tags, text);
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


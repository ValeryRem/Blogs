package main.controllers;

import main.entity.Session;
import main.entity.User;
import main.repository.PostRepository;
import main.repository.SessionRepository;
import main.repository.UserRepository;
import main.service.AuthService;
import main.service.GetService;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.RemoteSpringApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/post")
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

    @GetMapping("")
    @ResponseBody
    private ResponseEntity<?> getPosts (@RequestParam(defaultValue="0") Integer offset,
                                        @RequestParam(defaultValue="7") Integer limit,
                                        @RequestParam(defaultValue="recent") String mode){
        System.out.println("Method getPosts activated. Number of posts: " + getService.getCount());
        return getService.getPosts (offset, limit, mode);
    }

    @GetMapping("/{id:\\d+}")
    private ResponseEntity<?> getPostById (@PathVariable("id") Integer postId) {
        System.out.println("Method getPostById activated. ID requested: " + postId);
        return getService.getPostById(postId);
    }

    @GetMapping("/search/")
    private ResponseEntity<?> getPostsBySearch (@RequestParam(defaultValue = "new testing") String query,
                                               @RequestParam(defaultValue="0") Integer offset,
                                               @RequestParam(defaultValue="4") Integer limit,
                                               @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsBySearch activated. Query:" + query);
        return getService.getPostsBySearch(query, offset, limit, mode);
    }

    @GetMapping("/byDate")
    private ResponseEntity<?> getPostsByDate (
            @RequestParam(defaultValue = "2020-12-23")    @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date,
                                             @RequestParam(defaultValue="0") Integer offset,
                                             @RequestParam(defaultValue="5") Integer limit,
                                             @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByDate activated. Date:" + date );
        return getService.getPostsByDate(date, offset, limit, mode);
    }

    @GetMapping("/byTag")
    private ResponseEntity<?> getPostsByTag(@RequestParam(defaultValue = "PHP") String tagName,
                                            @RequestParam(defaultValue="0") Integer offset,
                                            @RequestParam(defaultValue="1") Integer limit,
                                            @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByTag uses tag name:" + tagName);
        return getService.getPostsByTag(tagName, offset, limit, mode);
    }

    @GetMapping("/moderation")
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

    @PostMapping("/like")
    private ResponseEntity<?> postLike (@RequestParam(defaultValue="5") Integer postToLikeId,
                                        @RequestParam(defaultValue="2") Integer userId) {
        System.out.println("Method postLike activated");
        return postService.postLike(postToLikeId, userId);
    }

    @PostMapping("/dislike")
    private ResponseEntity<?> postDislike (@RequestParam(defaultValue="5") Integer postToLikeId,
                                           @RequestParam(defaultValue="2") Integer userId) {
        System.out.println("Method postDislike activated");
        return postService.postDislike(postToLikeId, userId);
    }

    @PostMapping("")
    private ResponseEntity<?> postPost (@RequestParam(defaultValue="1") Integer active,
                                        @RequestParam(defaultValue="Optional.class description.") String title,
                                        @RequestParam(defaultValue="[Java, Python]") List<String> tags,
                                        @RequestParam(defaultValue="Try to consider how to implement Optional class " +
                                                "approach if behaviour of components is nullable.") String text) {
        System.out.println("userId: " + authService.getUserId());
        System.out.println("Method postPost is activated");
        return postService.postPost(active, title, tags, text);
    }

    @PutMapping("/{id:\\d+}")
    private ResponseEntity<?> putPost (@PathVariable("id") Integer postId,
                                       @RequestParam(defaultValue="1") Integer active,
                                       @RequestParam(defaultValue="Optional description.") String title,
                                       @RequestParam(defaultValue="Java, PHP, Excel, Darby, Python") List<String> tags,
                                       @RequestParam(defaultValue="Try to escape from here, and as soon as possible, my daring!") String text) {
        System.out.println("Method putPost is activated for postId: " + postId);
        return postService.putPost(postId, active, title, tags, text);
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


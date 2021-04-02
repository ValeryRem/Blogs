package main.controllers;

import main.repository.PostRepository;
import main.repository.SessionRepository;
import main.repository.UserRepository;
import main.requests.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @GetMapping("/{ID:\\d+}")
    //@RequestMapping(value = "/{ID: \\d+}", produces = "application/json", method = RequestMethod.GET)
    private ResponseEntity<?> getPostById (@PathVariable("ID") Integer ID) {
        System.out.println("Method getPostById activated. ID requested: " + ID);
        return getService.getPostById(ID);
    }

    @GetMapping("/search")
    private ResponseEntity<?> getPostsBySearch (
            @RequestParam(defaultValue="0") Integer offset,
            @RequestParam(defaultValue="4") Integer limit,
            @RequestParam String query) {
        System.out.println("Method getPostsBySearch activated. Query:" + query);
        return getService.getPostsBySearch(offset, limit, query);
    }

    @GetMapping("/byDate")
    private ResponseEntity<?> getPostsByDate (
            @RequestParam(defaultValue="0") Integer offset,
            @RequestParam(defaultValue="5") Integer limit,
            @RequestParam (defaultValue = "2021-03-30") LocalDate date){
        System.out.println("Method getPostsByDate activated by the date: " + date );
        return getService.getPostsByDate(offset, limit, date);
    }

    @GetMapping("/byTag")
    private ResponseEntity<?> getPostsByTag(
                                            @RequestParam Integer offset,
                                            @RequestParam Integer limit,
                                            @RequestParam String tag){
        System.out.println("Method getPostsByTag uses tag name:" + tag);
        return getService.getPostsByTag(offset, limit, tag);
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
    private ResponseEntity<?> postLike (@RequestBody LikeRequest likeRequest)
                                        {
        System.out.println("Method postLike activated");
        return postService.postLike(likeRequest.getPost_id());
    }

    @PostMapping("/dislike")
    private ResponseEntity<?> postDislike (@RequestBody DislikeRequest dislikeRequest)
    {
        System.out.println("Method postDislike activated");
        return postService.postDislike(dislikeRequest.getPost_id());
    }

    @PostMapping("")
    private ResponseEntity<?> postPost (@RequestBody PostRequest postRequest) {
        System.out.println("Method postPost is activated");
        return postService.postPost(postRequest.getTimestamp(), postRequest.getActive(), postRequest.getTitle(),
                postRequest.getTags(), postRequest.getText());
    }

    @PutMapping(value = "/{ID: \\d+}", //consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = "application/json") // produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> putPost (@PathVariable(value = "ID") int ID, @RequestBody PutPostRequest putPostRequest){
        System.out.println("Method putPost is activated");
        return postService.putPost(ID, putPostRequest.getTimestamp(), putPostRequest.getActive(), putPostRequest.getTitle(),
                putPostRequest.getTags(), putPostRequest.getText());
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


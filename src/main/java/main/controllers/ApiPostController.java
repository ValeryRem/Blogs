package main.controllers;

import main.service.GetService;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class ApiPostController {
    @Autowired
    private GetService getService;

    @Autowired
    private PostService postService;

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
    private ResponseEntity<?> getPostsByDate (@RequestParam(defaultValue = "2020-11-17")
                                                  @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date,
                                             @RequestParam(defaultValue="0") Integer offset,
                                             @RequestParam(defaultValue="5") Integer limit,
                                             @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByDate activated. Date:" + date );
        return getService.getPostsByDate(date, offset, limit, mode);
    }

    @GetMapping("/post/byTag")
    private ResponseEntity<?> getPostsByTag(@RequestParam(defaultValue = "#Java") String tagName,
                                            @RequestParam(defaultValue="0") Integer offset,
                                            @RequestParam(defaultValue="1") Integer limit,
                                            @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByTag uses tag name:" + tagName);
        return getService.getPostsByTag(tagName, offset, limit, mode);
    }

    @GetMapping("/post/my")
    private ResponseEntity<?> getMyPosts (@RequestParam(defaultValue="1") Integer myUserId, //defaultValue="1" to be deleted later on.
                                          @RequestParam(defaultValue="0") Integer offset,
                                          @RequestParam(defaultValue="5") Integer limit) {
        System.out.println("Method getMyPosts uses myUserId:" + myUserId);
        return getService.getMyPosts(myUserId, offset, limit);
    }

    @GetMapping("/post/moderation")
    private ResponseEntity<?> getPostsForModeration (@RequestParam(defaultValue="0") Integer offset,
                                                     @RequestParam(defaultValue="3") Integer limit,
                                                     @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsForModeration is activated.");
        return getService.getPostsForModeration(offset, limit, mode);
    }

    @PostMapping("/auth/login")
    private ResponseEntity<?> checkAuthLogin(@RequestParam(defaultValue="eee@jjj.hj") String userEmail,
                                             @RequestParam(defaultValue="pw1") String userPassword) {
        System.out.println("Method checkAuthLogin is activated.");
        return postService.checkAuthLogin(userEmail, userPassword);
    }

    @GetMapping("/auth/logout")
    private ResponseEntity<?> getAuthLogout (@RequestParam(defaultValue="1")Integer userId) {
        System.out.println("Method getAuthLogout is activated.");
        return getService.getAuthLogout(userId);
    }
}


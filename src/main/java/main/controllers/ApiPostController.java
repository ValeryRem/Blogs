package main.controllers;

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
    private PostService postService;

    @GetMapping("/post")
    @ResponseBody
    private ResponseEntity<?> getPosts (@RequestParam(defaultValue="0") Integer offset,
                                        @RequestParam(defaultValue="7") Integer limit,
                                        @RequestParam(defaultValue="recent") String mode){
        System.out.println("Method getPosts activated. Number of posts: " + postService.getCount());
        return postService.getPosts (offset, limit, mode);
    }

    @GetMapping("/post/{id:\\d+}")
    private ResponseEntity<?> getPostById (@PathVariable("id") Integer postId) {
        System.out.println("Method getPostById activated. ID requested: " + postId);
        return postService.getPostById(postId);
    }

    @GetMapping("/post/search")
    private ResponseEntity<?> getPostBySearch (@RequestParam(required=false) String query,
                                               @RequestParam(defaultValue="0") Integer offset,
                                               @RequestParam(defaultValue="3") Integer limit,
                                               @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsBySearch activated. Query:" + query);
        return postService.getPostsBySearch(query, limit, offset, mode);
    }

    @GetMapping("/post/byDate")
    private ResponseEntity<?> getPostByDate (@DateTimeFormat(pattern = "YYYY-MM-dd") LocalDate date,
                                             @RequestParam(defaultValue="0") Integer offset,
                                             @RequestParam(defaultValue="5") Integer limit,
                                             @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByDate activated. Date:" + date );
        return postService.getPostsByDate(date, offset, limit, mode);
    }

    @GetMapping("/post/byTag")
    private ResponseEntity<?> getPostsByTag(@RequestParam String tagName,
                                            @RequestParam(defaultValue="0") Integer offset,
                                            @RequestParam(defaultValue="1") Integer limit,
                                            @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByTag uses tag name:" + tagName);
        return postService.getPostsByTag(tagName, offset, limit, mode);
    }

    @GetMapping("/post/my")
    private ResponseEntity<?> getMyPosts (@RequestParam(defaultValue="1") Integer myUserId, //defaultValue="1" to be deleted later on.
                                          @RequestParam(defaultValue="0") Integer offset,
                                          @RequestParam(defaultValue="5") Integer limit) {
        System.out.println("Method getMyPosts uses myUserId:" + myUserId);
        return postService.getMyPosts(myUserId, offset, limit);
    }
}

package main.controllers;

import main.api.response.PostResponse;
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
    private PostResponse postResponse;

//    @Autowired
//    private static Storage storage;

    @GetMapping("/post")
    @ResponseBody
    private ResponseEntity<?> getPosts (@RequestParam(defaultValue="0") Integer offset,
                                        @RequestParam(defaultValue="5") Integer limit,
                                        @RequestParam(defaultValue="recent") String mode){
        System.out.println("Method getPosts activated. Number of posts: " + new PostService().getCount());
        return postResponse.getPosts (offset, limit, mode);
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
    private ResponseEntity<?> getPostsByTag(Integer tagId,
                                            @RequestParam(defaultValue="0") Integer offset,
                                            @RequestParam(defaultValue="5")Integer limit,
                                            @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByTag used. TagId:" + tagId );
        return postResponse.getPostByTag(tagId, offset, limit, mode);
    }
}

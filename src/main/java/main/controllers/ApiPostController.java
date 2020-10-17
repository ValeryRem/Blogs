package main.controllers;

import main.api.response.PostResponse;
import main.api.response.PostsListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class ApiPostController {
    @Autowired
    private PostResponse postResponse;
//    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
    private final Calendar calendar = Calendar.getInstance();
    private final Date currentDate = calendar.getTime();//formatter.getCalendar().getTime();

    @Autowired
    private PostsListResponse postsListResponse;

    @GetMapping("/post")
    @ResponseBody
    private ResponseEntity<?> getPosts (@RequestParam(defaultValue="0") Integer offset,
                                        @RequestParam(defaultValue="5") Integer limit,
                                        @RequestParam(defaultValue="recent") String mode){
        System.out.println("Method getPosts activated.");
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

    @GetMapping("/post/date")
    private ResponseEntity<?> getPostByDate (Date date,
                                             @RequestParam(defaultValue="0") Integer offset,
                                             @RequestParam(defaultValue="5")Integer limit,
                                             @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsByDate activated. Date:" + currentDate );
        return postResponse.getPostByDate(date, offset, limit, mode);
    }

}

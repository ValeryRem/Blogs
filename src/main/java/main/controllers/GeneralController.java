package main.controllers;

import main.api.response.InitResponse;
import main.entity.ModerationRequest;
import main.service.GetService;
import main.service.PostService;
import main.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GeneralController {

//    private final SettingsService settingsService;
    @Autowired
    private final InitResponse initResponse;

    @Autowired
    private GetService getService;

    @Autowired
    private PostService postService;

    @Autowired
    private final SettingsService settingsService;

    public GeneralController(SettingsService settingsService, InitResponse initResponse) {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
    }

    @PutMapping("/settings")
    private ResponseEntity<?> putApiSettings (@RequestParam(defaultValue = "true") boolean multiuserMode,
                                             @RequestParam(defaultValue = "true") boolean postPremoderation,
                                             @RequestParam(defaultValue = "false") boolean statisticsInPublic) {
        return settingsService.putApiSettings (multiuserMode, postPremoderation, statisticsInPublic);
    }

    @GetMapping("/settings")
    private ResponseEntity<?> getApiSettings () {
        return settingsService.getApiSettings();
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/tag/{query}")
    private ResponseEntity<?> getTag (@PathVariable("query") String query) {
        return getService.getTag(query);
    }

    @GetMapping("/tag")
    private ResponseEntity<?> getTag () {
        return getService.getTag();
    }

    @GetMapping("/statistics/my")
    private ResponseEntity <?> getMyStatistics () {
        return getService.getMyStatistics();
    }

    @GetMapping("/statistics/all")
    private ResponseEntity <?> getAllStatistics () {
        return getService.getAllStatistics ();
    }

    @GetMapping("/calendar/")
    private ResponseEntity <?> getApiCalendar (@RequestParam(defaultValue = "2017") Optional<Integer> year) {
        return getService.getApiCalendar (year);
    }

    @GetMapping("/moderation")
    private ResponseEntity<?> getPostsForModeration (@RequestParam(defaultValue="0") Integer offset,
                                                     @RequestParam(defaultValue="3") Integer limit,
                                                     @RequestParam(defaultValue="recent") String mode) {
        System.out.println("Method getPostsForModeration is activated.");
        return getService.getPostsForModeration(offset, limit, mode);
    }

    @PostMapping("/moderation")
    private ResponseEntity<?> postApiModeration (Integer postId, ModerationRequest request) {
        System.out.println("Method postApiModeration is activated.");
        return postService.postApiModeration(postId, request);
    }

    @PostMapping("/image")
    private ResponseEntity<?> postImage
            (@RequestParam(defaultValue="src/main/resources/static/img/default-1.png") String origin,
             @RequestParam(defaultValue= "upload/") String destination) throws IOException {
        System.out.println("Method postImage is activated.");
        return postService.postImage (origin, destination);
    }

    @PostMapping("/comment/")
    private ResponseEntity<?> postComment (@RequestParam(defaultValue="5") Integer postId,
                                           @RequestParam(defaultValue="") String parentId,
                                           @RequestParam(defaultValue="Ugly post.") String text) {
        System.out.println("Method postComment is activated.");
        return postService.postComment(4, "", text);
    }
}

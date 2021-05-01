package main.controllers;

import main.response.InitResponse;
import main.requests.CommentRequest;
import main.requests.PostModerationRequest;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeneralController {
    private final InitResponse initResponse;
    private final GetService getService;
    private final  PostService postService;
    private final SettingsService settingsService;
    private final  UserService userService;

    public GeneralController(InitResponse initResponse, GetService getService, PostService postService,
                             SettingsService settingsService, UserService userService) {
        this.initResponse = initResponse;
        this.getService = getService;
        this.postService = postService;
        this.settingsService = settingsService;
        this.userService = userService;
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

    @GetMapping("/calendar")
    private ResponseEntity <?> getApiCalendar (@RequestParam Integer year) {
        return getService.getApiCalendar (year);
    }

    @PostMapping("/moderation")
    private ResponseEntity<?> postApiModeration (@RequestBody PostModerationRequest postModerationRequest)
    {
        System.out.println("Method postApiModeration is activated.");
        return postService.postApiModeration(postModerationRequest.getPost_id(), postModerationRequest.getDecision());
    }

    @PostMapping(value = "/image", consumes = {"multipart/form-data"})// MediaType.MULTIPART_FORM_DATA_VALUE)
    public @ResponseBody ResponseEntity<?> postApiImage(@RequestBody MultipartFile image) throws IOException {
        System.out.println("Method postApiImage is activated.");
        return userService.postApiImage(image);
    }

    @PostMapping("/comment")
    private ResponseEntity<?> postComment (@RequestBody CommentRequest commentRequest){
        System.out.println("Method postComment is activated.");
        return postService.postComment(commentRequest);
    }
}

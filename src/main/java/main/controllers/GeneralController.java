package main.controllers;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.entity.ModerationRequest;
import main.service.GetService;
import main.service.PostService;
import main.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GeneralController {

    private final SettingsService settingsService;
    private final InitResponse initResponse;
    @Autowired
    private GetService getService;

    @Autowired
    private PostService postService;

    public GeneralController(SettingsService settingsService, InitResponse initResponse) {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/tag")
    private ResponseEntity<?> getTag (@RequestParam(defaultValue = "#PHP #Spring #Java")  String query) {
        return getService.getTag(query);
    }

    @GetMapping("/statistics/my")
    private ResponseEntity <?> getMyStatistics (@RequestParam(defaultValue = "1") Integer userId) {
        return getService.getMyStatistics(userId);
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

}

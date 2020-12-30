package main.controllers;

import main.api.response.InitResponse;
import main.entity.ModerationRequest;
import main.entity.User;
import main.service.GetService;
import main.service.PostService;
import main.service.SettingsService;
import main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    UserService userService;

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
    private ResponseEntity <?> getApiCalendar (@RequestParam(defaultValue = "2020") Optional<Integer> year) {
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
    private ResponseEntity<?> postApiModeration (@RequestParam(defaultValue="10")Integer postId,
                                                 @RequestParam(defaultValue="accept") String request) {
        System.out.println("Method postApiModeration is activated.");
        return postService.postApiModeration(postId, request);
    }

//    @PostMapping("/image")
//    private ResponseEntity<?> postImage
//            (@RequestParam(defaultValue="src/main/resources/static/img/default-1.png") String origin,
//             @RequestParam(defaultValue= "upload/") String destination) throws IOException {
//        System.out.println("Method postImage is activated.");
//        return postService.postImage (origin, destination);
//    }

    @PostMapping(value = "/image", consumes = {"multipart/form-data"})
    public @ResponseBody
    ResponseEntity<?> postApiImage(@RequestPart("image") MultipartFile image) throws IOException {
        return userService.postApiImage(image);
    }

    @PostMapping("/comment/")
    private ResponseEntity<?> postComment (@RequestParam(defaultValue="5") Integer postId,
                                           @RequestParam(defaultValue="") String parentId,
                                           @RequestParam(defaultValue="Ugly rude post. Method postComment is activated.") String text) {
        System.out.println("Method postComment is activated.");
        return postService.postComment(4, "", text);
    }
/*
{
  "photo": <binary_file>,
  "name":"Sendel",
  "email":"sndl@mail.ru",
  "password":"123456",
  "removePhoto":0
 */
    @PostMapping("/profile/my")
    private ResponseEntity<?> getPostProfileMy (
            @RequestParam(defaultValue="src/main/resources/static/img/default-1.png") Optional<String> photo,
            @RequestParam(defaultValue="Timur") String name,
            @RequestParam(defaultValue="kuka@ggg.ty") String email,
            @RequestParam(defaultValue="pw1212pop") Optional<String> password,
            @RequestParam(defaultValue="0") Optional<Integer> removePhoto) {
        System.out.println("Method getPostProfileMy is activated.");
        return postService.getPostProfileMy(photo, name, email, password, Optional.of(1));
    }
}

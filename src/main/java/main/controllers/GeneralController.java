package main.controllers;

import main.api.response.InitResponse;
import main.requests.CommentRequest;
import main.requests.PostModerationRequest;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeneralController {

    @Autowired
    private final InitResponse initResponse;

    @Autowired
    private GetService getService;

    @Autowired
    private PostService postService;

    @Autowired
    private final SettingsService settingsService;

    @Autowired
    private UserService userService;

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
    public @ResponseBody
    ResponseEntity<?> postApiImage(@RequestBody MultipartFile image) throws IOException {
        System.out.println("Method postApiImage is activated.");
        return userService.postApiImage(image);
    }

    @PostMapping("/comment")
    private ResponseEntity<?> postComment (@RequestBody CommentRequest commentRequest){
        System.out.println("Method postComment is activated.");
        return postService.postComment(commentRequest.getParent_id(), commentRequest.getPostId(), commentRequest.getText());
    }


// этот вариант нам подходит, если мы отправляем или форму или форму и картинку вместе
//    @PostMapping(value = "/profile/my", consumes = {"multipart/form-data", "application/json"})
//           // "application/x-www-form-urlencoded;charset=UTF-8"})
//    public ResponseEntity<?> postApiProfileMy (@org.jetbrains.annotations.NotNull @JsonProperty
//                                                   @ModelAttribute("profileRequest") ProfileRequest profileRequest)
//            throws IOException {
//            //@RequestBody(required = false) String requestBody, // тут можеть быть форма в json без картинки
////            @RequestPart(value = "photo")
////            @DefaultValue("src/main/resources/static/img/default-1.png")
////             MultipartFile avatar, // вот тут может быть картинка
////            @RequestPart(name = "email", required = false) String emailMP,
////            @RequestPart(name = "name", required = false) String nameMP,
////            @RequestPart(name = "password", required = false) String passwordMP,
////            @RequestPart(name = "remove_photo", required = false)
////            @DefaultValue("0")
////                    String removePhotoMP) throws IOException {
//        System.out.println("Method postApiProfileMy is activated.");
//        return userService.getPostProfileMy(profileRequest.getPhoto(), profileRequest.getEmail(), profileRequest.getName(),
//                profileRequest.getPassword(), profileRequest.getRemovePhoto());
//    }
}

package main.controllers;

import main.api.response.InitResponse;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

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

//    private File file = new File("src/main/resources/static/img/default-1.png");

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

    @PostMapping(value = "/image", consumes = {"multipart/form-data"})// MediaType.MULTIPART_FORM_DATA_VALUE)
    public //@ResponseBody
    ResponseEntity<?> postApiImage(@RequestParam("image") MultipartFile image) throws IOException {
        return userService.postApiImage(image);
    }

    @PostMapping("/comment/")
    private ResponseEntity<?> postComment (@RequestParam(defaultValue="5") Integer postId,
                                           @RequestParam(defaultValue="") String parentId,
                                           @RequestParam(defaultValue="Ugly rude post. Method postComment is activated.") String text) {
        System.out.println("Method postComment is activated.");
        return postService.postComment(4, "", text);
    }

// этот вариант нам подходит, если мы отправляем или форму или форму и картинку вместе
    @PostMapping(value = "/profile/my", consumes = {"multipart/form-data", "application/json"})
           // "application/x-www-form-urlencoded;charset=UTF-8"})
    public ResponseEntity<?> postApiProfileMy (@ModelAttribute LoginRequest loginRequest) throws IOException {
            //@RequestBody(required = false) String requestBody, // тут можеть быть форма в json без картинки
//            @RequestPart(value = "photo")
//            @DefaultValue("src/main/resources/static/img/default-1.png")
//             MultipartFile avatar, // вот тут может быть картинка
//            @RequestPart(name = "e_mail", required = false) String emailMP,
//            @RequestPart(name = "name", required = false) String nameMP,
//            @RequestPart(name = "password", required = false) String passwordMP,
//            @RequestPart(name = "remove_photo", required = false)
//            @DefaultValue("0")
//                    String removePhotoMP) throws IOException {
        return userService.getPostProfileMy(loginRequest.getAvatar(), loginRequest.getEmail(), loginRequest.getNameString(),
                loginRequest.getPassword(), loginRequest.getRemovePhoto());
    }
}

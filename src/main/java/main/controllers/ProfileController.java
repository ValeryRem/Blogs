package main.controllers;

import main.requests.ProfileRequest;
import main.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserService userService;
    private final ProfileRequest profileRequest;

    public ProfileController(UserService userService, ProfileRequest profileRequest) {
        this.userService = userService;
        this.profileRequest = profileRequest;
    }

    @PostMapping(value = "/my", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileRequest profileRequest) throws IOException {
        System.out.println("updateProfile method is active " + profileRequest.getEmail()); // для теста только возвращаем ответы и пишем в консоль
        return userService.getPostProfileMy(profileRequest);
    }

//    @PostMapping(value = "/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> updateProfileWithPhoto(@Valid @RequestBody ProfileRequest profileRequest) throws IOException {
//        System.out.println("updateProfile with Photo is active ");
//                return userService.getPostProfileMy(profileRequest);
//    }

    // этот вариант нам подходит, если мы отправляем или форму или форму и картинку вместе
//    @PostMapping(value = "/my", consumes = {"multipart/form-data", "application/json"})
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

package main.controllers;

import main.service.ProfileRequest;
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

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/my", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileRequest request) {
        System.out.println("updateProfile method is active " + request.getEmail()); // для теста только возвращаем ответы и пишем в консоль
        return ResponseEntity.ok("updateProfile method is active, " + request.getEmail());
    }

    @PostMapping(value = "/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfileWithPhoto(
//            ProfileRequest profileRequest
            @RequestParam("photo") MultipartFile photo, // это картинка
            @RequestParam("removePhoto") String removePhoto,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(name = "password", required = false) String password
    ) throws IOException {
        System.out.println("updateProfile with Photo is active " + photo.getOriginalFilename()); // для теста только возвращаем ответы и пишем в консоль
//        return ResponseEntity.ok("updateProfile with Photo is active " + photo.getOriginalFilename());
                return userService.getPostProfileMy(photo, email, name, password, removePhoto);
//                        profileRequest.getPhoto(), profileRequest.getEmail(), profileRequest.getName(),
//                profileRequest.getPassword(), profileRequest.getRemovePhoto());

    }
}

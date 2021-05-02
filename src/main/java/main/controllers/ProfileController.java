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

    @PostMapping(value = "/my", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileRequest request) throws IOException {
        System.out.println("updateProfile method is active " + request.getEmail()); // для теста только возвращаем ответы и пишем в консоль
        return userService.getPostProfileMy(request.getPhoto(), request.getEmail(), request.getName(),
                request.getPassword(), request.getRemovePhoto());
    }

    @PostMapping(value = "/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfileWithPhoto(
            @RequestParam("photo") MultipartFile photo, // это картинка
            @RequestParam("removePhoto") String removePhoto,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(name = "password", required = false) String password
    ) throws IOException {
        System.out.println("updateProfile with Photo is active " + photo.getOriginalFilename());
        return userService.getPostProfileMy(photo, email, name, password, removePhoto);
    }
}

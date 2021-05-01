package main.controllers;

import main.requests.RestoreRequest;
import main.service.AuthService;
import main.service.GetService;
import main.requests.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final GetService getService;
    private final AuthService authService;
    private final LoginRequest loginRequest;

    public AuthController(GetService getService, AuthService authService, LoginRequest loginRequest) {
        this.getService = getService;
        this.authService = authService;
        this.loginRequest = loginRequest;
    }

    @GetMapping("/check")
    private ResponseEntity<?> getAuthCheck () {
        System.out.println("Method getAuthCheck is activated.");
        return authService.getAuthCheck();
    }

    @PostMapping (value = "/login")//, consumes = {"application/json", "application/x-www-form-urlencoded;charset=UTF-8"})
    private ResponseEntity<?> postAuthLogin(@RequestBody LoginRequest loginRequest) {
        System.out.println("Method postAuthLogin is activated.");
        return authService.postAuthLogin(loginRequest);
    }

    @GetMapping("/logout")
    private ResponseEntity<?> getAuthLogout () {
        System.out.println("Method getAuthLogout is activated.");
        return authService.getAuthLogout();
    }

    @GetMapping("/captcha")
    private ResponseEntity<?> getCaptcha () {
        System.out.println("Method getCaptcha is activated.");
        return authService.getCaptcha();
    }

    @PostMapping("/register")
    private ResponseEntity<?> postAuthRegister(@RequestBody LoginRequest loginRequest){
        System.out.println("Method postAuthRegister is activated.");
        return authService.postAuthRegister(loginRequest);
    }

    @PostMapping("/restore")
    private ResponseEntity<?> authRestore (@RequestBody RestoreRequest restoreRequest) {
        System.out.println("Method authRestore is activated.");
        return authService.authRestore(restoreRequest.getEmail());
    }

    @PostMapping("/password")
    private ResponseEntity<?> authPassword (String code, String password, String captcha, String captchaSecret) {
        System.out.println("Method authPassword is activated.");
        return authService.authPassword(code, password, captcha, captchaSecret);
    }
}


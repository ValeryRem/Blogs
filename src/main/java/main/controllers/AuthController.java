package main.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.service.AuthService;
import main.service.GetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private GetService getService;

    @Autowired
    private AuthService authService;

    @GetMapping("/check")
    private ResponseEntity<?> getAuthCheck () {
        System.out.println("Method getAuthCheck is activated.");
        return authService.getAuthCheck();
    }

    @PostMapping("/login")
    private ResponseEntity<?> postAuthLogin(//@RequestParam (name = "e_mail")
                                            @JsonProperty ("e_mail") String eMail,
                                            @JsonProperty ("password") String userPassword) {
        System.out.println("Method postAuthLogin is activated.");
        return authService.postAuthLogin(eMail, userPassword);
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
    private ResponseEntity<?> postAuthRegister(@JsonProperty("e_mail") String eMail,
                                               @JsonProperty("password") String password,
                                               @JsonProperty("name") String nameString,
                                               @JsonProperty("captcha") String captcha,
                                               @JsonProperty("captcha_secret") String secretCaptcha) {
        System.out.println("Method postAuthRegister is activated.");
        return authService.postAuthRegister(eMail, password, nameString, captcha, secretCaptcha);
    }

    @PostMapping("/restore")
    private ResponseEntity<?> authRestore (@RequestParam(defaultValue="klo@ggg.ty") String eMail) {
        System.out.println("Method authRestore is activated.");
        return authService.authRestore(eMail);
    }

    @PostMapping("/password")
    private ResponseEntity<?> authPassword (String code, String password, String captcha, String captchaSecret) {
        System.out.println("Method authPassword is activated.");
        return authService.authPassword(code, password, captcha, captchaSecret);
    }
}


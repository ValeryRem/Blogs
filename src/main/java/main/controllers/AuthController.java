package main.controllers;

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
    private ResponseEntity<?> postAuthLogin(@RequestParam (value = "email")//, defaultValue="horn8@rr.tt")
                                                        String userEmail,
                                            @RequestParam (value = "password")//, defaultValue="pw8888")
                                                    String userPassword) {
        System.out.println("Method postAuthLogin is activated.");
        return authService.postAuthLogin(userEmail, userPassword);
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
    private ResponseEntity<?> postAuthRegister(@RequestParam(defaultValue="pony@kkkl.hj") String e_mail,
                                               @RequestParam(defaultValue="pw77982") String password,
                                               @RequestParam(defaultValue="Peter") String nameString,
                                               @RequestParam(defaultValue="govutigud") String captcha,
                                               @RequestParam(defaultValue="yapaponep") String secret_captcha) {
        System.out.println("Method postAuthRegister is activated.");
        return authService.postAuthRegister(e_mail, password, nameString, captcha, secret_captcha);
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


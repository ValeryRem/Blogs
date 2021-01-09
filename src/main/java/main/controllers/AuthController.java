package main.controllers;

import main.service.AuthService;
import main.service.GetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private GetService getService;

    @Autowired
    private AuthService authService;

    @GetMapping("/check")
    private ResponseEntity<?> getAuthCheck (HttpSession session) {
        System.out.println(session.getId());
        return authService.getAuthCheck();
    }

    @PostMapping("/login")
    private ResponseEntity<?> postAuthLogin(@RequestParam(defaultValue="reka1@ggg.ty") String userEmail,
                                               @RequestParam(defaultValue="ttt778") String userPassword) {
        System.out.println("Method postAuthLogin is activated.");
        return authService.postAuthLogin(userEmail, userPassword);
    }

    @GetMapping("/logout")
    private ResponseEntity<?> getAuthLogout (@RequestParam(defaultValue="1")Integer userId) {
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
        return authService.authPassword("Nk0uuWP8H3PAEPg1", "pw1212", "zohicimova", "laceqacig");
    }
}


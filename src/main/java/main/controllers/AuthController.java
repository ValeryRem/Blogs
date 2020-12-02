package main.controllers;

import main.service.AuthSevice;
import main.service.GetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private GetService getService;

    @Autowired
    private AuthSevice authSevice;

    @GetMapping("/check")
    private ResponseEntity<?> getAuthCheck (HttpSession session) {
        System.out.println(session.getId());
        return authSevice.getAuthCheck();
    }

    @PostMapping("/login")
    private ResponseEntity<?> postAuthLogin(@RequestParam(defaultValue="eee@jjj.hj") String userEmail,
                                               @RequestParam(defaultValue="pw1") String userPassword) {
        System.out.println("Method postAuthLogin is activated.");
        return authSevice.postAuthLogin(userEmail, userPassword);
    }

    @GetMapping("/logout")
    private ResponseEntity<?> getAuthLogout (@RequestParam(defaultValue="1")Integer userId) {
        System.out.println("Method getAuthLogout is activated.");
        return authSevice.getAuthLogout();
    }

    @GetMapping("/captcha")
    private ResponseEntity<?> getCaptcha () throws IOException {
        System.out.println("Method getCaptcha is activated.");
        return authSevice.getCaptcha();
    }

    @PostMapping("/register")
    private ResponseEntity<?> postAuthRegister(@RequestParam(defaultValue="pony@kkkl.hj") String e_mail,
                                               @RequestParam(defaultValue="pw77982") String password,
                                               @RequestParam(defaultValue="Peter") String nameString,
                                               @RequestParam(defaultValue="govutigud") String captcha,
                                               @RequestParam(defaultValue="yapaponep") String secret_captcha) {
        System.out.println("Method postAuthRegister is activated.");
        return authSevice.postAuthRegister(e_mail, password, nameString, captcha, secret_captcha);
    }
}


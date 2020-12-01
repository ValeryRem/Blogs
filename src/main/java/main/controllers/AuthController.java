package main.controllers;

import main.service.AuthSevice;
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

}


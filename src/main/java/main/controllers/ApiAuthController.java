package main.controllers;

import main.service.GetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    @Autowired
    private GetService getService;

//    @GetMapping("check")
//    private ResponseEntity<?> getAuthCheck () {
//        return ResponseEntity.ok(new ResultResponse(false));
//    }

    @GetMapping("/check")
    private ResponseEntity<?> getAuthCheck (@RequestParam(defaultValue="1") Integer userId) {
        return getService.getAuthCheck(userId);
    }

}


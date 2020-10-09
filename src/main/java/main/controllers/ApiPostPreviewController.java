package main.controllers;

import main.api.response.PostPreviewResponse;
import main.api.response.PostResponse;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class ApiPostPreviewController {
    @Autowired
    private PostPreviewResponse postPreviewResponse;

    @GetMapping("/preview")
    private ResponseEntity<?> getPostPreviews () {
        return postPreviewResponse.getPostPreviewResponse();
    }
}

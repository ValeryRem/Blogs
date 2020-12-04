package main.service;

import main.api.response.SettingsResponse;
import main.entity.*;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class PostService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    AuthSevice authSevice;
    private boolean result = false;

    @Autowired
    HttpSession httpSession;

    @Autowired
    SettingsResponse settingsResponse;
    ResponseEntity<?> responseEntity;


    public ResponseEntity<?> postApiModeration (Integer postId, ModerationRequest decision) {
        if (authSevice.isUserAuthorized()) {
            Post post = postRepository.getOne(postId);
            if (decision.equals(ModerationRequest.ACCEPT)) {
                post.setModerationStatus(ModerationStatus.ACCEPTED);
                result = true;
                responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
            } else if (decision.equals(ModerationRequest.DECLINE)) {
                post.setModerationStatus(ModerationStatus.DECLINED);
                responseEntity = new ResponseEntity<>(result, HttpStatus.NOT_MODIFIED);
            } else {
                responseEntity = new ResponseEntity<>("Wrong request!", HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            responseEntity = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

}

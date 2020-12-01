package main.service;

import main.entity.*;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;


    public ResponseEntity<?> postApiModeration (Integer postId, ModerationRequest decision) {
        boolean result = false;
        Post post = postRepository.getOne(postId);
        ResponseEntity<?> responseEntity;
        if(decision.equals(ModerationRequest.ACCEPT)) {
            post.setModerationStatus(ModerationStatus.ACCEPTED);
            result = true;
            responseEntity = new ResponseEntity<>(result, HttpStatus.OK);
        } else if (decision.equals(ModerationRequest.DECLINE)) {
            post.setModerationStatus(ModerationStatus.DECLINED);
            responseEntity = new ResponseEntity<>(result, HttpStatus.NOT_MODIFIED);
        } else {
            responseEntity = new ResponseEntity<>("Wrong request!", HttpStatus.NOT_ACCEPTABLE);
        }
        return responseEntity;
    }
}

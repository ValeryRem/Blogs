package main.service;

import main.api.response.SettingsResponse;
import main.entity.*;
import main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    PostVoteRepository postVoteRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    Tag2PostRepository tag2PostRepository;
    ResponseEntity<?> responseEntity;


    public ResponseEntity<?> postApiModeration (Integer postId, ModerationRequest decision) {
        if (authSevice.isUserAuthorized(httpSession.getId())) {
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

    public ResponseEntity<?> postLike (Integer postToLikeId, Integer userId) {
        if (authSevice.isUserAuthorized(httpSession.getId())) {
            List<PostVote> postVotes = postVoteRepository.findAll().stream().
                    filter(pv -> pv.getPostId().equals(postToLikeId)).
                    collect(Collectors.toList());
            PostVote postVote = postVotes.stream().filter(pv -> pv.getUserId().equals(userId)).findAny().orElse(new PostVote());
            if(postVote.getValue() != 1) {
                postVote.setValue(1);
                postVoteRepository.save(postVote);
                responseEntity = new ResponseEntity<>("result: true", HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("result: false", HttpStatus.ALREADY_REPORTED);
            }
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    public ResponseEntity<?> postDislike (Integer postToLikeId, Integer userId) {
        if (authSevice.isUserAuthorized(httpSession.getId())) {
            List<PostVote> postVotes = postVoteRepository.findAll().stream().
                    filter(pv -> pv.getPostId().equals(postToLikeId)).
                    collect(Collectors.toList());
            PostVote postVote = postVotes.stream().filter(pv -> pv.getUserId().equals(userId)).findAny().orElse(new PostVote());
            if(postVote.getValue() == 1) {
                postVote.setValue(0);
                postVoteRepository.save(postVote);
                responseEntity = new ResponseEntity<>("result: true", HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("result: false", HttpStatus.ALREADY_REPORTED);
            }
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    public ResponseEntity<?> postPost (long timestamp, Integer active, String title, List<String> tags, String text) {
        if(authSevice.isUserAuthorized(httpSession.getId())) {
            if (title.length() < 3 || text.length() < 50) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Post post = new Post();
            post.setTime(LocalDate.ofEpochDay(timestamp));
            post.setIsActive(active);
            post.setTitle(title);
            post.setText(text);
            postRepository.save(post);
            Tag2Post tag2Post;
            for (String tag: tags) {
                if(tagRepository.findAll().stream().map(t -> t.getName().equals(tag)).findAny().isEmpty()) {
                    Tag tagNew = new Tag(tag);
                    tagRepository.save(tagNew);
                    tag2Post = new Tag2Post(post.getPostId(), tagNew.getId());
                    tag2PostRepository.save(tag2Post);
                }
            }
            responseEntity = new ResponseEntity<>("result: true", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

}

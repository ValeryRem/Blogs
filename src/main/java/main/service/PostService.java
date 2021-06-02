package main.service;

import main.requests.*;
import main.response.ErrorsResponse;
import main.response.ResultResponse;
import main.entity.*;
import main.repository.*;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final HttpSession httpSession;
    private final PostVoteRepository postVoteRepository;
    private final TagRepository tagRepository;
    private final Tag2PostRepository tag2PostRepository;
    private final CommentRepository commentRepository;
    private final GlobalSettingsRepository globalSettingsRepository;
    private final CommentRequest commentRequest;
    private final PostRequest postRequest;
    private final PutPostRequest putPostRequest;
//    private final LikeRequest likeRequest;
//    private final DislikeRequest dislikeRequest;

    private ResponseEntity<?> responseEntity;

    public PostService(UserRepository userRepository, PostRepository postRepository, AuthService authService,
                       HttpSession httpSession, PostVoteRepository postVoteRepository, TagRepository tagRepository,
                       Tag2PostRepository tag2PostRepository, CommentRepository commentRepository,
                       GlobalSettingsRepository globalSettingsRepository, CommentRequest commentRequest,
                       PostRequest postRequest, PutPostRequest putPostRequest){//}, LikeRequest likeRequest,
                       //DislikeRequest dislikeRequest) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.authService = authService;
        this.httpSession = httpSession;
        this.postVoteRepository = postVoteRepository;
        this.tagRepository = tagRepository;
        this.tag2PostRepository = tag2PostRepository;
        this.commentRepository = commentRepository;
        this.globalSettingsRepository = globalSettingsRepository;
        this.commentRequest = commentRequest;
        this.postRequest = postRequest;
        this.putPostRequest = putPostRequest;
//        this.likeRequest = likeRequest;
//        this.dislikeRequest = dislikeRequest;
    }

    public ResponseEntity<?> postApiModeration(Integer id, String decision) {
        ResultResponse resultResponse = new ResultResponse(false);
        if (!authService.isUserAuthorized()) {
            return new ResponseEntity<>(resultResponse, HttpStatus.UNAUTHORIZED);
        }
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            if (decision.equals("accept")) {
                optionalPost.get().setModerationStatus(ModerationStatus.ACCEPTED);
                resultResponse = new ResultResponse(true);
                responseEntity = new ResponseEntity<>(resultResponse, HttpStatus.OK);
            } else if (decision.equals("decline")) {
                optionalPost.get().setModerationStatus(ModerationStatus.DECLINED);
                responseEntity = new ResponseEntity<>(resultResponse, HttpStatus.NOT_MODIFIED);
            }
            postRepository.save(optionalPost.get());
        } else {
            responseEntity = new ResponseEntity<>(resultResponse, HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    public ResponseEntity<?> postLikeDislike(Integer postId, Integer value) {
        User user = userRepository.getOne(authService.getUserId());
//        Integer postId = likeRequest.getPostId();
        Post post = postRepository.getOne(postId);

        if (!authService.isUserAuthorized()) {
            return new ResponseEntity<>(new ResultResponse(false), HttpStatus.OK);
        }

        if (post.getUserId().equals(user.getUserId())) {
            return new ResponseEntity<>(new ResultResponse(false), HttpStatus.OK);
        }

        postVoteRepository.save(createLikeDislike(postId, value));
        responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
        return responseEntity;
    }

    private PostVote createLikeDislike(Integer postId, Integer value){    //LikeRequest likeRequest) {
        PostVote postVote = new PostVote();
        postVote.setPostId(postId);   //likeRequest.getPostId());
        postVote.setTime(Timestamp.valueOf(now()));
        postVote.setUserId(authService.getUserId());
        postVote.setValue(value);
        return postVote;
    }

//    public ResponseEntity<?> postDislike (DislikeRequest dislikeRequest) {
//        User user = userRepository.getOne(authService.getUserId());
//        Post post = postRepository.getOne(dislikeRequest.getPostId());
//        if (authService.isUserAuthorized() && !post.getUserId().equals(user.getUserId())) {
//            PostVote postVote = new PostVote();
//            postVote.setPostId(dislikeRequest.getPostId());
//            postVote.setTime(Timestamp.valueOf(now()));
//            postVote.setUserId(authService.getUserId());
//            postVote.setValue(-1);
//            postVoteRepository.save(postVote);
//            responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
//        } else {
//            responseEntity = new ResponseEntity<>(new ResultResponse(false), HttpStatus.UNAUTHORIZED);
//        }
//        return responseEntity;
//    }

    public ResponseEntity<?> postPost(PostRequest postRequest) {
    Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
    User user = userRepository.getOne(authService.getUserId());
    Map<String, String> errors = checkTexts(postRequest.getTitle(), postRequest.getText());
    Map<String, Object> responseMap = new LinkedHashMap<>();
    if (!errors.isEmpty()) {
        responseMap.put("result", false);
        responseMap.put("errors", errors);
    return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
    if (!authService.isUserAuthorized()) {
    return new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }
    Post post = new Post();
    post.setIsActive(postRequest.getActive());
    Optional<User> optModerator = userRepository.findAll().stream().filter(User::getIsModerator).findAny();
    optModerator.ifPresent(value -> post.setModeratorId(value.getUserId()));
    if (postRequest.getTimestamp() <= currentTimestamp.getTime() / 1000) {
        post.setTimestamp(currentTimestamp);
    } else {
        post.setTimestamp(new Timestamp(postRequest.getTimestamp() * 1000));
    }
    post.setUserId(authService.getUserId());
    post.setViewCount(0);
    post.setTitle(postRequest.getTitle());
    post.setText(cleanedOffHtml(postRequest.getText()));
    if (globalSettingsRepository.findAll().stream().findAny().orElse(new GlobalSettings()).isPostPremoderation()) {
        if (!user.getIsModerator() || postRequest.getActive() != 1) { // if the user is not moderator
            post.setModerationStatus(ModerationStatus.NEW);
        }
        if (user.getIsModerator() && postRequest.getActive() == 1) {
            post.setModerationStatus(ModerationStatus.ACCEPTED);
        }
    } else {
        post.setModerationStatus(ModerationStatus.ACCEPTED);
    }
    postRepository.save(post);
    processTags(postRequest.getTags(), post, postRequest.getTitle(), postRequest.getText());
    responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
    return responseEntity;
}

    private void processTags(List<String> tags, Post post, String title, String text) {
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        for (String tag : tags) {
            if (!tagRepository.findAll().stream()
                                .map(Tag::getTagName)
                                .collect(Collectors.toList())
                                .contains(tag)){
                if (title.contains(tag) || text.contains(tag)) {
                    Tag tagNew = new Tag(tag);
                    tagRepository.save(tagNew);
                    Tag2Post tag2Post = new Tag2Post(post.getPostId(), tagNew.getId());
                    tag2PostRepository.save(tag2Post);
                }
            }
        }
    }

    public ResponseEntity<?> putPost(int id, PutPostRequest putPostRequest) {
        if (!authService.isUserAuthorized()) {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        Map<String, Object> responseMap = new LinkedHashMap<>();
        Map<String, String> errors = checkTexts(putPostRequest.getTitle(), putPostRequest.getText());
        if (!errors.isEmpty()) {
            ErrorsResponse errorsResponse = new ErrorsResponse(errors);
            responseMap.put("result", String.valueOf(false));
            responseMap.put("errors", errorsResponse);
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
        Optional<Post> postOptional = postRepository.findById(id);
        if(postOptional.isEmpty()) {
            return new ResponseEntity<>(new ResultResponse(false), HttpStatus.NOT_FOUND);
        }
        Post post = postOptional.get();
        User user = userRepository.getOne(post.getUserId());
        post.setText(cleanedOffHtml(putPostRequest.getText()));
        post.setTitle(putPostRequest.getTitle());
        post.setActive(putPostRequest.getActive());
        long currentTime = Instant.now().toEpochMilli();
        if (putPostRequest.getTimestamp() <= currentTime) {
            post.setTimestamp(new Timestamp(currentTime));
        }
        if(!user.getIsModerator()) {
            post.setModerationStatus(ModerationStatus.NEW);
        }
        postRepository.save(post);
        processTags(putPostRequest.getTags(), post, putPostRequest.getTitle(), putPostRequest.getText());
        responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
        return responseEntity;
    }

    private Map<String, String> checkTexts (String title, String text) {
        Map<String, String> errors = new LinkedHashMap<> ();
        if (title.length() < 3) {
            errors.put("title", "Заголовок слишком короткий");
        } else
            if (title.length()  > 100) {
            errors.put("title", "Заголовок слишком длинный!");
        }
        if (text.length() < 30) {
            errors.put("text", "Текст публикации слишком короткий");
        } else
            if(text.length() > 1000) {
            errors.put("text", "Текст публикации слишком длинный!");
        }
            return errors;
    }

    public ResponseEntity<?> postComment(CommentRequest commentRequest) {
        boolean result = true;
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Map<String, String> errors = new LinkedHashMap<>();
        if (!authService.isUserAuthorized()) {
            errors.put("errors", "User is unauthorized!");
            map.put("result", new ResultResponse(false));
            map.put("errors", errors);
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        Integer userId = authService.getUserId();
        PostComment postComment = new PostComment();
        if (commentRequest.getText().length() < 10 || commentRequest.getText().length() > 300) {
            result = false;
            errors.put("text", "Text's length is out of limit!");
        }
        if (result) {
            if (commentRequest.getParentId() != null) {
                postComment.setParentId(commentRequest.getParentId());
            }
            postComment.setPostId(commentRequest.getPostId());
            postComment.setText(commentRequest.getText());
            postComment.setTime(Timestamp.valueOf(now()));
            postComment.setUserId(userId);
            commentRepository.save(postComment);
            map.put("id", postComment.getCommentId());
        } else {
            map.put("result", new ResultResponse(false));
            map.put("errors", errors);
        }
        responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        return responseEntity;
    }

    public String cleanedOffHtml (String input) {
        return Jsoup.parse(input).text();
    }

}

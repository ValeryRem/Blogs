package main.service;

import main.response.ErrorsResponse;
import main.response.GeneralResponse;
import main.response.ResultResponse;
import main.entity.*;
import main.repository.*;
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

    final UserRepository userRepository;
    final PostRepository postRepository;
    final AuthService authService;
    final HttpSession httpSession;
    final PostVoteRepository postVoteRepository;
    final TagRepository tagRepository;
    final Tag2PostRepository tag2PostRepository;
    final CommentRepository commentRepository;
    final GlobalSettingsRepository globalSettingsRepository;

    private ResponseEntity<?> responseEntity;

    public PostService(UserRepository userRepository, PostRepository postRepository, AuthService authService,
                       HttpSession httpSession, PostVoteRepository postVoteRepository, TagRepository tagRepository,
                       Tag2PostRepository tag2PostRepository, CommentRepository commentRepository,
                       GlobalSettingsRepository globalSettingsRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.authService = authService;
        this.httpSession = httpSession;
        this.postVoteRepository = postVoteRepository;
        this.tagRepository = tagRepository;
        this.tag2PostRepository = tag2PostRepository;
        this.commentRepository = commentRepository;
        this.globalSettingsRepository = globalSettingsRepository;
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

    public ResponseEntity<?> postLike (Integer post_id) {
        User user = userRepository.getOne(authService.getUserId());
        Post post = postRepository.getOne(post_id);
        if (authService.isUserAuthorized() && !post.getUserId().equals(user.getUserId())) {
            PostVote postVote = new PostVote();
            postVote.setPostId(post_id);
            postVote.setTime(Timestamp.valueOf(now()));
            postVote.setUserId(authService.getUserId());
            postVote.setValue(1);
            postVoteRepository.save(postVote);
            responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new ResultResponse(false), HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    public ResponseEntity<?> postDislike (Integer post_id) {
        User user = userRepository.getOne(authService.getUserId());
        Post post = postRepository.getOne(post_id);
        if (authService.isUserAuthorized() && !post.getUserId().equals(user.getUserId())) {
            PostVote postVote = new PostVote();
            postVote.setPostId(post_id);
            postVote.setTime(Timestamp.valueOf(now()));
            postVote.setUserId(authService.getUserId());
            postVote.setValue(-1);
            postVoteRepository.save(postVote);
            responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new ResultResponse(false), HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }
/*
POST_PREMODERATION - если включен этот режим, то все новые посты пользователей с moderation = true обязательно
должны попадать на модерацию, у постов при создании должен быть установлен moderation_status = NEW. Eсли значения
POST_PREMODERATION = false (режим премодерации выключен), то все новые посты должны сразу публиковаться (если у них
установлен параметр active = 1), у постов при создании должен быть установлен moderation_status = ACCEPTED.
*/
public ResponseEntity<?> postPost(long timestamp, Integer active, String title, List<String> tags, String text) {
    Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
    User user = userRepository.getOne(authService.getUserId());
    Map<String, String> errors = checkTexts(title, text);
    Map<String, Object> responseMap = new LinkedHashMap<>();
    if (!errors.isEmpty()) {
        responseMap.put("result", String.valueOf(false));
        ErrorsResponse errorsResponse = new ErrorsResponse(errors);
        responseMap.put("errors", errorsResponse);
    return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
    if (!authService.isUserAuthorized()) {
    return new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }
    Post post = new Post();
    post.setIsActive(active);
    // назначаем id любого из модераторов при создании нового поста
    Optional<User> optModerator = userRepository.findAll().stream().filter(User::getIsModerator).findAny();
    optModerator.ifPresent(value -> post.setModeratorId(value.getUserId()));
    ////
    if (timestamp <= currentTimestamp.getTime() / 1000) {
        post.setTimestamp(currentTimestamp);
    } else {
        post.setTimestamp(new Timestamp(timestamp * 1000));
    }
    post.setUserId(authService.getUserId());
    post.setViewCount(0);
    post.setTitle(title);
    post.setText(text);
    if (globalSettingsRepository.findAll().stream().
            findAny().
            orElse(new GlobalSettings()).
            isPostPremoderation()) {
        if (!user.getIsModerator() || active != 1) { // if the user is not moderator
            post.setModerationStatus(ModerationStatus.NEW);
        }
        if (user.getIsModerator() && active == 1) {
            post.setModerationStatus(ModerationStatus.ACCEPTED);
        }
        postRepository.save(post);

    } else {
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        postRepository.save(post);
    }
    processTags(tags, post, title, text);
    responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
    return responseEntity;
}

    private void processTags(List<String> tags, Post post, String title, String text) {
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        for (String tag : tags) {
            if (!tagRepository.findAll().stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList())
                    .contains(tag)) {
                if (title.contains(tag) || text.contains(tag)) {
                    Tag tagNew = new Tag(tag);
                    tagRepository.save(tagNew);
                    Tag2Post tag2Post = new Tag2Post(post.getPostId(), tagNew.getId());
                    tag2PostRepository.save(tag2Post);
                }
            }
        }
    }

    public ResponseEntity<?> putPost(int ID, long timestamp, Integer active, String title, List<String> tags, String text) {
        if (!authService.isUserAuthorized()) {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        Map<String, Object> responseMap = new LinkedHashMap<>();
        Map<String, String> errors = checkTexts(title, text);
        GeneralResponse generalResponse = new GeneralResponse();
        if (!errors.isEmpty()) {
            ErrorsResponse errorsResponse = new ErrorsResponse(errors);
            responseMap.put("result", String.valueOf(false));
            responseMap.put("errors", errorsResponse);
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        }
        Post post = postRepository.getOne(ID);
        User user = userRepository.getOne(post.getUserId());
        post.setText(text);
        post.setTitle(title);
        post.setActive(active);
        long currentTime = Instant.now().toEpochMilli();
        if (timestamp <= currentTime) {
            post.setTimestamp(new Timestamp(currentTime));
        }
        if(!user.getIsModerator()) {
            post.setModerationStatus(ModerationStatus.NEW);
        }
        postRepository.save(post);
        processTags(tags, post, title, text);
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

    public ResponseEntity<?> postComment(Integer parent_id, Integer post_id, String text) {
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
        if (text.length() < 10 || text.length() > 300) {
            result = false;
            errors.put("text", "Text's length is out of limit!");
        }
        if (result) {
            if (parent_id != null) {
                postComment.setParentId(parent_id);
            }
            postComment.setPostId(post_id);
            postComment.setText(text);
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
}

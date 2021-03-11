package main.service;

import main.api.response.ErrorsResponse;
import main.api.response.GeneralResponse;
import main.api.response.ResultResponse;
import main.entity.*;
import main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
public class PostService {

    @Autowired
    ErrorsResponse errorsResponse;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    AuthService authService;
    private boolean result = false;

    @Autowired
    HttpSession httpSession;

    @Autowired
    PostVoteRepository postVoteRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    Tag2PostRepository tag2PostRepository;

    @Autowired
    PostCommentRepository postCommentRepository;

    @Autowired
    GlobalSettingsReporitory globalSettingsReporitory;
    private final Integer PW_MIN_LENGTH = 6;
    private final Integer PW_MAX_LENGTH = 30;

    private ResponseEntity<?> responseEntity;
    private GeneralResponse generalResponse;

//    private final ZoneId zid1 = ZoneId.of("Europe/Moscow");

    public ResponseEntity<?> postApiModeration (Integer postId, String decision) {
        if (authService.isUserAuthorized()) {
            Post post = postRepository.getOne(postId);
            if (decision.equals("accept")) {
                post.setModerationStatus(ModerationStatus.ACCEPTED);
                responseEntity = new ResponseEntity<>("result: true", HttpStatus.OK);
            } else if (decision.equals("decline")) {
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
        if (authService.isUserAuthorized()) {
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
        if (authService.isUserAuthorized()) {
            List<PostVote> postVotes = postVoteRepository.findAll().stream().
                    filter(pv -> pv.getPostId().equals(postToLikeId)).
                    collect(Collectors.toList());
            PostVote postVote = postVotes.stream().filter(pv -> pv.getUserId().equals(userId)).findAny().orElse(new PostVote());
            if(postVote.getValue() == 1) {
                postVote.setValue(0);
                postVoteRepository.save(postVote);
                responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(new ResultResponse(false), HttpStatus.ALREADY_REPORTED);
            }
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }
/*
POST_PREMODERATION - если включен этот режим, то все новые посты пользователей с moderation = false обязательно
должны попадать на модерацию, у постов при создании должен быть установлен moderation_status = NEW. Eсли значения
POST_PREMODERATION = false (режим премодерации выключен), то все новые посты должны сразу публиковаться (если у них
установлен параметр active = 1), у постов при создании должен быть установлен moderation_status = ACCEPTED.
*/
    public ResponseEntity<?> postPost (long timestamp, Integer active, String title, List<String> tags, String text) {
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        result = true;
        User user = userRepository.getOne(authService.getUserId());
        Map<String, Object> errors = new LinkedHashMap<>();
        Post post = new Post();
        post.setIsActive(active);
        post.setModeratorId(1);

        if(timestamp <= currentTimestamp.getTime()/1000) {
            post.setTimestamp(currentTimestamp);
        } else {
            post.setTimestamp(new Timestamp(timestamp*1000));
        }
        post.setUserId(authService.getUserId());
        post.setViewCount(0);
        checkTexts(title, text, errors);
        if (!result) {
            errorsResponse.getErrors().put("errors", errors);
            return new ResponseEntity<>(errorsResponse.getErrors(), HttpStatus.BAD_REQUEST);
        } else {
            post.setTitle(title);
            post.setText(text);
            if (authService.isUserAuthorized()) {
                if (globalSettingsReporitory.findAll().stream().
                        findAny().
                        orElse(new GlobalSettings()).
                        isPostPremoderation()) { // проверка POST_PREMODERATION = true
                    if (!user.getIsModerator()) { // if the user is not moderator
                        post.setModerationStatus(ModerationStatus.NEW);
                        responseEntity =  new ResponseEntity<>("Waiting for moderation.", HttpStatus.OK);
                    } else { // if the user is moderator the posy saved
                        post.setModerationStatus(ModerationStatus.ACCEPTED);
//                        postRepository.save(post);
                        Tag2Post tag2Post;
                        for (String tag : tags) {
                            if (tagRepository.findAll().stream().map(t -> t.getTagName().equals(tag)).findAny().isEmpty()) {
                                Tag tagNew = new Tag(tag);
                                tagRepository.save(tagNew);
                                tag2Post = new Tag2Post(post.getPostId(), tagNew.getId());
                                tag2PostRepository.save(tag2Post);
                            }
                        }
                        responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
                    }
                } else { // if POST_PREMODERATION = false
                    if (active == 1) {
                        post.setModerationStatus(ModerationStatus.ACCEPTED);

                        Tag2Post tag2Post;
                        for (String tag : tags) {
                            if (tagRepository.findAll().stream().map(t -> t.getTagName().equals(tag)).findAny().isEmpty()) {
                                Tag tagNew = new Tag(tag);
                                tagRepository.save(tagNew);
                                tag2Post = new Tag2Post(post.getPostId(), tagNew.getId());
                                tag2PostRepository.save(tag2Post);
                            }
                        }
                        responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
                    } else { //  if (active != 1)
                        post.setModerationStatus(ModerationStatus.NEW);
                        responseEntity =  new ResponseEntity<>("Waiting for moderation.", HttpStatus.NOT_ACCEPTABLE);
                    }
                }
                postRepository.save(post);
            } else {
                responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
            }
            return responseEntity;
        }
    }

//    public ResponseEntity<?> postImage (String origin) throws IOException {
//        String destination = "/avatars/";
//        String hashCode = String.valueOf(Math.abs(destination.hashCode()));
//        String folder1 = hashCode.substring(0, hashCode.length()/3);
//        String folder2 = hashCode.substring(1 + hashCode.length()/3, 2*hashCode.length()/3);
//        String folder3 = hashCode.substring(1 + 2*hashCode.length()/3);
//        if (authService.isUserAuthorized()) {
//            try {
//                File originalFile = new File(origin);
//                BufferedImage image = ImageIO.read(originalFile);
//                int suffix = (int) (Math.random() * 100);
//                File destFolder = new File(destination);
//                if (!destFolder.exists()) {
//                    destFolder.mkdir();
//                }
//                File destFolder1 = new File(destination + folder1);
//                if (!destFolder1.exists()) {
//                    destFolder1.mkdir();
//                }
//                File destFolder2 = new File (destination + folder1 + File.separator + folder2);
//                if (!destFolder2.exists()) {
//                    destFolder2.mkdir();
//                }
//                String finalDestination = destination + folder1 + "/" + folder2 + "/" + folder3 + "/";
//                File destFolder3 = new File (finalDestination);
//                if (!destFolder3.exists()) {
//                    destFolder3.mkdir();
//                }
//                String fileName =  suffix + "_uploaded.jpg";
//                    File output = new File(destFolder3, fileName);
//                    ImageIO.write(image, "jpg", output);
//                    responseEntity = new ResponseEntity<>(finalDestination + fileName, HttpStatus.OK);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                responseEntity = new ResponseEntity<>("No image loaded!", HttpStatus.NOT_FOUND);
//            }
//        } else {
//            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
//        }
//        return responseEntity;
//    }

    public ResponseEntity<?> putPost(Integer postId, Integer active, String title, List<String> tags, String text) {
        result = true;
        Map<String, Object> errors = new LinkedHashMap<>();
        if (authService.isUserAuthorized()) {
            checkTexts(title, text, errors);
            if (!result) {
                errorsResponse.getErrors().put("errors", errors);
                return new ResponseEntity<>(errorsResponse.getErrors(), HttpStatus.BAD_REQUEST);
            } else {
                try {
                    Post post = postRepository.getOne(postId);
                    post.setText(text);
                    post.setTitle(title);
                    post.setActive(active);
                    post.setTimestamp(Timestamp.valueOf(now()));
                    postRepository.save(post);
                    List<String> tagNames = tagRepository.findAll().stream().map(Tag::getTagName).collect(Collectors.toList());
                    List<Tag2Post> oldItems = tag2PostRepository.findAll().stream().
                            filter(t -> t.getPostId().equals(postId)).
                            collect(Collectors.toList());
                    List<Tag2Post> newItems = new ArrayList<>();
                    for (String tagName : tags) {
                        if (!tagNames.contains(tagName)) {
                            Tag tag = new Tag(tagName);
                            tagRepository.save(tag);
                            Integer tagId = tag.getId();
                            Tag2Post tag2Post = new Tag2Post(postId, tagId);
                            tag2PostRepository.save(tag2Post);
                            newItems.add(new Tag2Post(postId, tagId));
                        }
                    }
                    for (Tag2Post t2p : oldItems) {
                        if (!newItems.contains(t2p)) {
                            tag2PostRepository.delete(t2p);
                        }
                    }
                    responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    responseEntity = new ResponseEntity<>("PostId " + postId + " is absent.", HttpStatus.NOT_FOUND);
                }
            }
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    private void checkTexts (String title, String text, Map<String, Object> errors) {
        if (title.length() < 3) {
            result = false;
            errors.put("Title", "Заголовок слишком короткий");
        }
        if (text.length() < 50) {
            result = false;
            errors.put("Text", "Текст публикации слишком короткий");
        }
    }

    public ResponseEntity<?> postComment(Integer postId, String parentId, String text) {
        result = true;
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> errors = new LinkedHashMap<>();
        int parentIdInt;
        if(parentId.length() > 0) {
            parentIdInt = Integer.parseInt(parentId);
        } else {
            parentIdInt = 0;
        }
        if (authService.isUserAuthorized()) {
            Integer userId = authService.getUserId();
            PostComment postComment = new PostComment();
            if (postRepository.findAll().stream().
                    map(Post::getPostId).
                    collect(Collectors.toList()).
                    contains(postId)) {
                postComment.setPostId(postId);
            } else {
                result = false;
                errors.put("Post with id ", postId + " does not exist.");
                errorsResponse.getErrors().put("errors", errors);
            }
            if (postCommentRepository.findAll().stream().
                    map(PostComment::getCommentId).
                    collect(Collectors.toList()).
                    contains(parentIdInt)) {
                postComment.setParentId(parentIdInt);
            } else {
                if (parentIdInt > 0) {
                    result = false;
                    errors.put("ParentId ", parentId + " does not exist.");
                    errorsResponse.getErrors().put("errors", errors);
                } else {
                    postComment.setParentId(parentIdInt);
                }
            }
            if (text.length() < 20) {
                result = false;
                errors.put("Text", "Text too short!");
                errorsResponse.getErrors().put("errors", errors);
            }
            if (result) {
                postComment.setTime(now());
                postComment.setUserId(userId);
                postComment.setText(text);
                postCommentRepository.save(postComment);
                map.put("id", postComment.getCommentId());
                responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(errorsResponse.getErrors(), HttpStatus.OK);
            }
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

}

package main.service;

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
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
public class PostService {

//    @Autowired
    ResultResponse resultResponse = new ResultResponse(false);
//    ErrorsResponse errorsResponse;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    AuthService authService;
//    private boolean result = false;

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

    public ResponseEntity<?> postApiModeration (Integer id, String decision) {
//        ResultResponse resultResponse = new ResultResponse(false);
        if (authService.isUserAuthorized()) {
            Optional<Post> optionalPost = postRepository.findById(id);
            if(optionalPost.isPresent()) {
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
        } else {
            responseEntity = new ResponseEntity<>(resultResponse, HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    public ResponseEntity<?> postLike (Integer post_id) {
        if (authService.isUserAuthorized()) {
            PostVote postVote = new PostVote();
            postVote.setPostId(post_id);
            postVote.setTime(Timestamp.valueOf(now()));
            postVote.setUserId(authService.getUserId());
            postVote.setValue(1);
            postVoteRepository.save(postVote);
            responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
//            } else {
//                responseEntity = new ResponseEntity<>("result: false", HttpStatus.ALREADY_REPORTED);
//            }
//        } else {
//            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        } else {
            responseEntity = new ResponseEntity<>(new ResultResponse(false), HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    public ResponseEntity<?> postDislike (Integer post_id) {
        if (authService.isUserAuthorized()) {
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
    public ResponseEntity<?> postPost (long timestamp, Integer active, String title, List<String> tags, String text) {
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        User user = userRepository.getOne(authService.getUserId());
        LinkedHashMap<String, Object> errors = checkTexts(title, text);
        Post post = new Post();
        post.setIsActive(active);
//        post.setModeratorId(1); // to be in the input parameters

        if(timestamp <= currentTimestamp.getTime()/1000) {
            post.setTimestamp(currentTimestamp);
        } else {
            post.setTimestamp(new Timestamp(timestamp*1000));
        }
        post.setUserId(authService.getUserId());
        post.setViewCount(0);

        if (!errors.isEmpty())
        {
            resultResponse = new ResultResponse(false);
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
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
                        responseEntity =  new ResponseEntity<>("Waiting for moderation.", HttpStatus.OK);
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

    public ResponseEntity<?> putPost(Long timestamp, Integer isActive, String title, List<String> tags, String text, Integer ID) {
        LinkedHashMap<String, Object> errors = checkTexts(title, text);
        Map<String, Object> responseMap = new LinkedHashMap<>();
        if (authService.isUserAuthorized()) {
            System.out.println("putService: " + title); // test
            Post post = postRepository.getOne(ID);
//                Optional<Post> optionalPost = postRepository.findAll().stream()
//                        .filter(p -> p.getTitle().equals(title) && (p.getTimestamp().getTime()/1000) == timestamp).findAny();
//                    if(optionalPost.isPresent()) {
//                        Post post = optionalPost.get();
//                        int postId = post.getPostId();
                        post.setText(text);
                        post.setTitle(title);
                        post.setActive(isActive);
                        post.setTimestamp(new Timestamp(timestamp * 1000));
                        postRepository.save(post);
                        List<String> tagNames = tagRepository.findAll().stream().map(Tag::getTagName).collect(Collectors.toList());
                        List<Tag2Post> oldItems = tag2PostRepository.findAll().stream().
                                filter(t -> t.getPostId().equals(ID)).
                                collect(Collectors.toList());
                        List<Tag2Post> newItems = new ArrayList<>();
                        for (String tagName : tags) {
                            if (!tagNames.contains(tagName)) {
                                Tag tag = new Tag(tagName);
                                tagRepository.save(tag);
                                Integer tagId = tag.getId();
                                Tag2Post tag2Post = new Tag2Post(ID, tagId);
                                tag2PostRepository.save(tag2Post);
                                newItems.add(new Tag2Post(ID, tagId));
                            }
                        }
                        for (Tag2Post t2p : oldItems) {
                            if (!newItems.contains(t2p)) {
                                tag2PostRepository.delete(t2p);
                            }
                        }
//                    } else {
//                       errors.put("post", "Not found!");
//                    }
            if (!errors.isEmpty()) {
                resultResponse = new ResultResponse(false);
                responseMap.put("result", resultResponse);
                responseMap.put("errors", errors);
                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            } else {
                    responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
            }
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    private LinkedHashMap<String, Object> checkTexts (String title, String text) {
        LinkedHashMap<String, Object> errors = new LinkedHashMap<> ();
        if (title.length() < 3) {
            errors.put("Title", "Заголовок слишком короткий");
        } else
            if (title.length()  > 100) {
            errors.put("Title", "Заголовок слишком длинный!");
        }
        if (text.length() < 50) {
            errors.put("Text", "Текст публикации слишком короткий");
        } else
            if(text.length() > 1000) {
            errors.put("Text", "Текст публикации слишком длинный!");
        }
            return errors;
    }

    public ResponseEntity<?> postComment(Integer parent_id, Integer post_id, String text) {
        boolean result = true;
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        LinkedHashMap<String, Object> errors = new LinkedHashMap<>();
        if (authService.isUserAuthorized()) {
            Integer userId = authService.getUserId();
            PostComment postComment = new PostComment();
            if (text.length() < 10 || text.length() > 300 ) {
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
                postCommentRepository.save(postComment);
                map.put("id", postComment.getCommentId());
            } else {
                map.put("result", resultResponse);
                map.put("errors", errors);
            }
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            map.put("result", new ResultResponse(false));
            map.put("errors", errors);
            responseEntity = new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

}

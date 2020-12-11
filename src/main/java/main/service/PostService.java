package main.service;

import main.api.response.SettingsResponse;
import main.entity.*;
import main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    AuthService authService;
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
        if (authService.isUserAuthorized()) {
            Post post = postRepository.getOne(postId);
            if (decision.equals(ModerationRequest.ACCEPT)) {
                post.setModerationStatus(ModerationStatus.ACCEPTED);
                responseEntity = new ResponseEntity<>("result: true", HttpStatus.OK);
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
                responseEntity = new ResponseEntity<>("result: true", HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("result: false", HttpStatus.ALREADY_REPORTED);
            }
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    public ResponseEntity<?> postPost (LocalDate time, Integer active, String title, List<String> tags, String text) {
        if(authService.isUserAuthorized()) {
            if (title.length() < 3 || text.length() < 50) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Post post = new Post();
            post.setTime(time);
            post.setIsActive(active);
            post.setTitle(title);
            post.setText(text);
            post.setModerationStatus(ModerationStatus.NEW);
            post.setModeratorId(1);
            post.setTime(LocalDate.now());
            post.setUserId(authService.getUserId());
            post.setViewCount(33);
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

    public ResponseEntity<?> postImage (String origin, String destination) throws IOException {
        String hashCode = "" + destination.hashCode();
        String folder1 = hashCode.substring(0, hashCode.length()/3);
        String folder2 = hashCode.substring(1 + hashCode.length()/3, 2*hashCode.length()/3);
        String folder3 = hashCode.substring(1 + 2*hashCode.length()/3);
        if (authService.isUserAuthorized()) {
            try {
                File originalFile = new File(origin);
                BufferedImage image = ImageIO.read(originalFile);
                int suffix = (int) (Math.random() * 100);
                File destFolder = new File(destination);
                if (!destFolder.exists()) {
                    destFolder.mkdir();
                }
                File destFolder1 = new File(destination + folder1);
                if (!destFolder1.exists()) {
                    destFolder1.mkdir();
                }
                File destFolder2 = new File (destination + folder1 + "/" + folder2);
                if (!destFolder2.exists()) {
                    destFolder2.mkdir();
                }
                String finalDestination = destination + folder1 + "/" + folder2 + "/" + folder3 + "/";
                File destFolder3 = new File (finalDestination);
                if (!destFolder3.exists()) {
                    destFolder3.mkdir();
                }
                String fileName =  suffix + "_uploaded.jpg";
                    File output = new File(destFolder3, fileName);
                    ImageIO.write(image, "jpg", output);
                    responseEntity = new ResponseEntity<>(finalDestination + fileName, HttpStatus.OK);
            } catch (IOException ex) {
                ex.printStackTrace();
                responseEntity = new ResponseEntity<>("No image loaded!", HttpStatus.NOT_FOUND);
            }
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }
}

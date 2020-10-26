package main.service;

import main.entity.ModerationStatus;
import main.entity.Post;
import main.entity.Tag;
import main.repository.PostRepository;
import main.repository.Tag2PostRepository;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Service
public class PostService {
    private Integer count;
    private List<Post> postList;
    private Iterable<Post> posts;
    private ResponseEntity<?> responseEntity;
    private  List<Object> objectList; // output for listToShow

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Tag2PostRepository tag2PostRepository;

    @Autowired
    private Tag tag;

    @Autowired
    private TagRepository tagRepository;

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        objectList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList :: add);
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        for (Post post: sortedPosts) {
            objectList.add(new PostToShow(post));
        }
        return getResponseEntity(objectList, offset, limit);
    }

    public ResponseEntity<?> getPostById(Integer postId) {
        try {
            Post post = postRepository.findById(postId).get();
            return new ResponseEntity<>(post, HttpStatus.FOUND);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>("Post with ID = " + postId + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getPostsBySearch(String query, Integer offset, Integer limit, String mode) {
        objectList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList :: add);
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        if (query == null) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (Post post : sortedPosts) {
                if (post.getText().contains(query) && post.getIsActive() == 1 && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                    objectList.add(new PostToShow(post));
                }
            }
            responseEntity = getResponseEntity(objectList, offset, limit);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getPostsByDate(LocalDate time, Integer offset, Integer limit, String mode) {
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList :: add);
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        objectList = new ArrayList<>();
        for (Post post : sortedPosts) {
            if (post.getTime().equals(time) && post.getIsActive() == 1 &&
                    post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                objectList.add(post);
            }
        }
        return getResponseEntity(objectList, offset, limit);
    }

    public ResponseEntity<?> getPostsByTag(@RequestParam Integer tagId, Integer offset, Integer limit, String mode) {
//        String tagName = tagRepository.findById(tagId).get();
//        tag = new Tag(tagName);
//        Integer tagId = tag.getId();
        List<Integer> postsId = new ArrayList<>();
        tag2PostRepository.findAllById(Collections.singleton(tagId)).forEach(postsId::add);
        List<Post> postList = new ArrayList<>();
        for (Integer postId : postsId) {
            postList.add(postRepository.findById(postId).get());
        }
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        objectList = new ArrayList<>();
        objectList.add(sortedPosts);
        return getResponseEntity(objectList, offset, limit);
    }

    private List<Post> getSortedPosts(List<Post> postList, String mode) {
        switch (mode) {
            case "popular":
                postList.sort(Comparator.comparing(Post::getViewCount).reversed());
                break;
            case "best":
                postList.sort(Comparator.comparing(Post::getLikeCount).reversed());
                break;
            case "early":
                postList.sort(Comparator.comparing(Post::getTime));
                break;
            default:
                postList.sort(Comparator.comparing(Post::getTime).reversed());
        }
        return postList;
    }

    private ResponseEntity<?> getResponseEntity(List<Object> objectList, Integer offset, Integer limit) {
        count = objectList.size();
        if (count == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //  output for ResponseEntity
        List<Object> listToShow = new ArrayList<>();
        if (limit <= count) {
            listToShow.add(count);
            listToShow.add(objectList.subList(offset, limit));
        } else {
            listToShow.add(count);
            listToShow.add(objectList.subList(offset, count));
        }
        return new ResponseEntity<>(listToShow, HttpStatus.FOUND);
    }

    public Integer getCount() {
        return count;
    }
}

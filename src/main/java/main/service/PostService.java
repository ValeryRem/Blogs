package main.service;

import main.entity.*;
import main.repository.PostRepository;
import main.repository.Tag2PostRepository;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
//@ComponentScan(basePackages = {"entity", "repository"})
public class PostService {
    private Integer count;
    private Integer tagId;
    private ResponseEntity<?> responseEntity;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Tag2PostRepository tag2PostRepository;

    @Autowired
    private Tag2Post tag2Post;

    @Autowired
    private Tag tag;

    @Autowired
    private TagRepository tagRepository;

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        List<Object> objectList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList :: add);
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        for (Post post: sortedPosts) {
            objectList.add(getPostToShow(post));
        }
        return getResponseEntity(objectList, offset, limit);
    }

    public ResponseEntity<?> getPostById(Integer postId) {
        try {
            Post post = postRepository.findById(postId).get();
            return new ResponseEntity<>(getPostToShow(post), HttpStatus.FOUND);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>("Post with ID = " + postId + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getPostsBySearch(String query, Integer offset, Integer limit, String mode) {
        List<Object> objectList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList :: add);
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        if (query == null) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (Post post : sortedPosts) {
                if (post.getText().contains(query) && post.getIsActive() == 1 && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                    objectList.add(getPostToShow(post));
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
        List<Object> objectList = new ArrayList<>();
        for (Post post : sortedPosts) {
            if (post.getTime().equals(time) && post.getIsActive() == 1 &&
                    post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                objectList.add(getPostToShow(post));
            }
        }
        return getResponseEntity(objectList, offset, limit);
    }
//Field tag2Post in main.service.PostService required a bean of type 'main.entity.Tag2Post' that could not be found.
    public ResponseEntity<?> getPostsByTag(String tagName, Integer offset, Integer limit, String mode) {
        Iterable<Tag> iterableTags = tagRepository.findAll();
        for(Tag tag: iterableTags) {
            if(tag.getName().equals(tagName)) {
                tagId = tag.getId();
                break;
            }
        }
        List<Integer> postsId = new ArrayList<>();
        Iterable<Tag2Post> tag2PostIterable = tag2PostRepository.findAll();
        for (Tag2Post tag2Post : tag2PostIterable) {
            if(tag2Post.getTagId().equals(tagId)) {
                postsId.add(tag2Post.getPostId());
            }
        }
        List<Post> posts = new ArrayList<>();
        for(Integer postId : postsId) {
            Post post = postRepository.findById(postId).get();
            posts.add(post);
        }
        List<Post> sortedPosts = getSortedPosts(posts, mode);
        List<Object>  objectList = new ArrayList<>();
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

    private List<Object> getPostToShow(Post post) {
        List<Object> postToShow = new ArrayList<>();
        postToShow.add(post.getPostId());
        postToShow.add(post.getTime());
        postToShow.add(post.getTitle());
        postToShow.add(post.getAnnounce());
        List<Object> userToShow = new ArrayList<>();
        userToShow.add(post.getUserId());
        User user = new User(post.getUserId());
        userToShow.add(user.getName());
        postToShow.add(userToShow);
        postToShow.add(post.getLikeCount());
        postToShow.add(post.getViewCount());
        return postToShow;
    }
}

package main.service;

import main.entity.*;
import main.repository.PostRepository;
import main.repository.Tag2PostRepository;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class PostService {
    private Integer tagId;

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

    public PostService() {
    }

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
            LinkedHashMap  <String, Object> postToShow = new LinkedHashMap<>();
            if(post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
            post.getTime().compareTo(LocalDate.now()) <= 0 ) {
                postToShow.putAll(getPostToShow(post));
                postToShow.put("comments", post.getComments());
                Iterable<Tag2Post> tag2PostIterable = tag2PostRepository.findAll();
                List<Integer> tagsId = new ArrayList<>();
                for (Tag2Post tag2Post : tag2PostIterable) {
                    if(tag2Post.getPostId().equals(postId)) {
                       tagsId.add(tag2Post.getTagId());
                    }
                }
                List<String> tags = new ArrayList<>();
                Iterable<Tag> iterableTags = tagRepository.findAll();
                for(Tag tag: iterableTags) {
                    if(tagsId.contains(tag.getId())) {
                        tags.add(tag.getName());
                    }
                }
                postToShow.put("tags", tags);
            }
            return new ResponseEntity<>(postToShow, HttpStatus.FOUND);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>("Post with ID = " + postId + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getPostsBySearch(String query, Integer offset, Integer limit, String mode) {
        List<Object> objectList = new ArrayList<>();
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList :: add);
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        ResponseEntity<?> responseEntity;
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
        ResponseEntity<?> responseEntity;
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
        if (objectList.size() == 0) {
            responseEntity = new ResponseEntity<>("Post with the date " + time + " not found", HttpStatus.NOT_FOUND);
        } else {
            responseEntity = getResponseEntity(objectList, offset, limit);
        }
        return responseEntity;
    }

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

        Integer countOfPosts = getCount();
        if (countOfPosts == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Object> listToShow = new ArrayList<>();
        TreeMap<String, Integer> countMap = new TreeMap<>();
        countMap.put("count", countOfPosts);
        if (limit <= countOfPosts) {
            listToShow.add(objectList.subList(offset, limit));
        } else {
            listToShow.add(objectList.subList(offset, countOfPosts));
        }
        TreeMap<String, Object> treeMap = new TreeMap<>(countMap);
        treeMap.put("posts", listToShow);
        return new ResponseEntity<>(treeMap, HttpStatus.FOUND);
    }

    public Integer getCount() {
        int count;
        try {
            List<Post> postList = new ArrayList<>();
            postRepository.findAll().forEach(postList :: add);
            count = postList.size();
        } catch (NullPointerException ex){
            count = 0;
        }
        return count;
    }

    private LinkedHashMap  <String, Object> getPostToShow(Post post) {
        LinkedHashMap  <String, Object> postToShow = new LinkedHashMap <>();
        postToShow.put("id", post.getPostId());
        postToShow.put("timestamp", post.getTime());
        List<Object> userToShow = new ArrayList<>();
        userToShow.add(post.getUserId());
        User user = new User(post.getUserId());
        userToShow.add(user.getName());
        postToShow.put("user", userToShow);
        postToShow.put("title", post.getTitle());
        postToShow.put("announce", post.getAnnounce());
        postToShow.put("likeCount", post.getLikeCount());
        postToShow.put("dislikeCount", post.getDislikeCount());
        postToShow.put("commentCount", post.getComments().size());
        postToShow.put("viewCount", post.getViewCount());
        return postToShow;
    }
}

package main.service;

import main.api.response.MyPostResponce;
import main.api.response.PostAnnounceResponse;
import main.api.response.PostByIdResponce;
import main.api.response.PostsListResponse;
import main.entity.*;
import main.repository.PostRepository;
import main.repository.Tag2PostRepository;
import main.repository.TagRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
public class PostService {
    private Integer tagId;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Tag2PostRepository tag2PostRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    public PostService() {
    }

    private List<Post> getPostList() {
        return  new ArrayList<>(postRepository.findAll());
    }

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        List<Post> postList = getPostList();
        List<PostAnnounceResponse> posts = new ArrayList<>();
        for (Post post : getSortedPosts(postList, mode)) {
            posts.add(new PostAnnounceResponse(post));
        }
        PostsListResponse postsListResponse = new PostsListResponse(getCount(), posts);
        return getResponseEntity(postsListResponse, offset, limit);
    }

    public ResponseEntity<?> getPostsBySearch(String query, Integer offset, Integer limit, String mode) {
        List<Post> postList = getPostList();
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        List<PostAnnounceResponse> posts = new ArrayList<>();
        ResponseEntity<?> responseEntity;
        if (query == null) {
            responseEntity = new ResponseEntity<>("Posts with the query " + query + " not found", HttpStatus.NOT_FOUND);
        } else {
            for (Post post : sortedPosts) {
                if (post.getText().contains(query) && post.getIsActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                    posts.add(new PostAnnounceResponse(post));
                }
            }
            responseEntity = getResponseEntity(new PostsListResponse(postList.size(), posts), offset, limit);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getPostsByDate(LocalDate time, Integer offset, Integer limit, String mode) {
        List<Post> postList = getPostList();
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        List<PostAnnounceResponse> posts = new ArrayList<>();
        ResponseEntity<?> responseEntity;
        for (Post post : sortedPosts) {
            if (post.getTime().equals(time) && post.getIsActive() &&
                    post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                posts.add(new PostAnnounceResponse(post));
            }
        }
        if (posts.size() == 0) {
            responseEntity = new ResponseEntity<>("Post with the date " + time + " not found", HttpStatus.NOT_FOUND);
        } else {
            responseEntity = getResponseEntity(new PostsListResponse(postList.size(), posts), offset, limit);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getPostsByTag(String tagName, Integer offset, Integer limit, String mode) {
        ResponseEntity<?> responseEntity;
        try {
            Iterable<Tag> iterableTags = tagRepository.findAll();
            for (Tag tag : iterableTags) {
                if (tag.getName().equals(tagName)) {
                    tagId = tag.getId();
                    break;
                }
            }
            List<Integer> postsId = new ArrayList<>();
            Iterable<Tag2Post> tag2PostIterable = tag2PostRepository.findAll();
            for (Tag2Post tag2Post : tag2PostIterable) {
                if (tag2Post.getTagId().equals(tagId)) {
                    postsId.add(tag2Post.getPostId());
                }
            }
            List<Post> posts = new ArrayList<>();
            List<Post> sortedPosts = getSortedPosts(posts, mode);
            List<PostAnnounceResponse> postsList = new ArrayList<>();
            for (Integer postId : postsId) {
                Post post = postRepository.findById(postId).get();
                postsList.add(new PostAnnounceResponse(post));
            }
            responseEntity = getResponseEntity(new PostsListResponse(sortedPosts.size(), postsList), offset, limit);
        } catch (Exception ex) {
            responseEntity = new ResponseEntity<>("Tag " + tagName + " not found!", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getMyPosts(Integer myUserId, Integer offset, Integer limit) {
        ResponseEntity<?> responseEntity;
        List<Post> posts = getPostList();
        List<MyPostResponce> myPostsList = new ArrayList<>();
        TreeMap<String, Object> map = new TreeMap<>();
        int count = 0;
        for (Post post : posts) {
            if (post.getUserId().equals(myUserId))
//                    && (post.getIsActive() == 0
//                    || ((post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.NEW))
//                    || (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.DECLINED))
//                    || (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED)))))
            {
                myPostsList.add(new MyPostResponce(post));
                count++;
            }
        }
        if (count == 0) {
            responseEntity = new ResponseEntity<>("No my posts", HttpStatus.NOT_FOUND);
        } else {
            if (limit <= count) {
                myPostsList = myPostsList.subList(offset, limit);
            } else {
                myPostsList = myPostsList.subList(offset, count);
            }
            map.put("count", count);
            map.put("posts", myPostsList);
            responseEntity = new ResponseEntity<>(map, HttpStatus.FOUND);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getPostById(Integer postId) {
//        List<Post> posts = getPostList();
        try {
           Post post = postRepository.getOne(postId);
           User user = new User(post.getUserId());//userRepository.getOne(post.getUserId());//
           PostByIdResponce postByIdResponce = new PostByIdResponce(post, user);
//            for (Post p : posts) {
//                if (p.getPostId().equals(postId)) {
//                    post = p;
//                    break;
//                }
//            }
            if (post.getIsActive() && post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
                    post.getTime().compareTo(LocalDate.now()) <= 0) {
                Iterable<Tag2Post> tag2PostIterable = tag2PostRepository.findAll();
                List<Integer> tagsId = new ArrayList<>();
                for (Tag2Post tag2Post : tag2PostIterable) {
                    if (tag2Post.getPostId().equals(postId)) {
                        tagsId.add(tag2Post.getTagId()); // формируем лист id тэгов, связанных с postId
                    }
                }
                Iterable<Tag> iterableTags = tagRepository.findAll();
                for (Tag tag : iterableTags) {
                    if (tagsId.contains(tag.getId())) {
                        postByIdResponce.getTags().add(tag); // добавляем тэги в объект вывода
                    }
                }
            }
            return new ResponseEntity<>(postByIdResponce, HttpStatus.FOUND);
        } catch (Exception ex) {
            System.err.println("Что-то пошло не так...");
            return new ResponseEntity<>("Post with ID = " + postId + " not found.", HttpStatus.NOT_FOUND);
        }
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

    private ResponseEntity<?> getResponseEntity(PostsListResponse postsListResponse, Integer offset, Integer limit) {
        ResponseEntity<?> responseEntity;
        Integer countOfPosts = getCount();
        List<PostAnnounceResponse> listToShow = postsListResponse.getPosts();
        if (countOfPosts == 0) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if (limit <= countOfPosts) {
                listToShow = listToShow.subList(offset, limit);
            } else {
                listToShow = listToShow.subList(offset, countOfPosts);
            }
            responseEntity = new ResponseEntity<>(new PostsListResponse(getCount(), listToShow), HttpStatus.FOUND);
        }
        return responseEntity;
    }

    public Integer getCount() {
        int count;
        try {
            List<Post> postList = getPostList();
            count = postList.size();
        } catch (NullPointerException ex) {
            count = 0;
        }
        return count;
    }
}
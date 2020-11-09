package main.service;

import main.api.response.PostAnnounceResponse;
import main.api.response.PostsListResponse;
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
    private TagRepository tagRepository;

    public PostService() {
    }

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        List<Post> postList = getPostList();
        List<PostAnnounceResponse> posts = new ArrayList<>();
        for (Post post : getSortedPosts(postList, mode)) {
          posts.add(new PostAnnounceResponse(post));
        }
        PostsListResponse postsListResponse = new PostsListResponse(postList.size(), posts);
        return getResponseEntity(postsListResponse, offset, limit);
    }

    private List<Post> getPostList() {
        List<Post> postList = new ArrayList<>();
        postRepository.findAll().forEach(postList::add);
        return postList;
    }


//    public ResponseEntity<?> getPostById(Integer postId) {
//        try {
//            Post post = postRepository.findById(postId).get();
////            LinkedHashMap  <String, Object> postToShow = new LinkedHashMap<>();
//            if(post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
//            post.getTime().compareTo(LocalDate.now()) <= 0 ) {
//                postToShow.putAll(getPostToShow(post));
//                postToShow.put("comments", post.getComments());
//                Iterable<Tag2Post> tag2PostIterable = tag2PostRepository.findAll();
//                List<Integer> tagsId = new ArrayList<>();
//                for (Tag2Post tag2Post : tag2PostIterable) {
//                    if(tag2Post.getPostId().equals(postId)) {
//                       tagsId.add(tag2Post.getTagId());
//                    }
//                }
//                List<String> tags = new ArrayList<>();
//                Iterable<Tag> iterableTags = tagRepository.findAll();
//                for(Tag tag: iterableTags) {
//                    if(tagsId.contains(tag.getId())) {
//                        tags.add(tag.getName());
//                    }
//                }
//                postToShow.put("tags", tags);
//            }
//            return new ResponseEntity<>(postToShow, HttpStatus.FOUND);
//        } catch (NoSuchElementException ex) {
//            return new ResponseEntity<>("Post with ID = " + postId + " not found.", HttpStatus.NOT_FOUND);
//        }
//    }

    public ResponseEntity<?> getPostsBySearch(String query, Integer offset, Integer limit, String mode) {
        List<Post> postList = getPostList();
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        List<PostAnnounceResponse> posts = new ArrayList<>();
        ResponseEntity<?> responseEntity;
        if (query == null) {
            responseEntity = new ResponseEntity<>("Posts with the query " + query + " not found", HttpStatus.NOT_FOUND);
        } else {
            for (Post post : sortedPosts) {
                if (post.getText().contains(query) && post.getIsActive() == 1 && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
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
            if (post.getTime().equals(time) && post.getIsActive() == 1 &&
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
            List<Post> sortedPosts = getSortedPosts(posts, mode);
            List<PostAnnounceResponse> postsList = new ArrayList<>();
            for(Integer postId : postsId) {
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
        List<PostAnnounceResponse> postsList = new ArrayList<>();
        for (Post post : posts) {
            if (post.getUserId().equals(myUserId))
//                    && (post.getIsActive() == 0
//                    || ((post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.NEW))
//                    || (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.DECLINED))
//                    || (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED)))))
                {
                postsList.add(new PostAnnounceResponse(post));
            }
        }
        responseEntity = getResponseEntity(new PostsListResponse(postsList.size(), postsList), offset, limit);
        return responseEntity;
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
        } catch (NullPointerException ex){
            count = 0;
        }
        return count;
    }
}

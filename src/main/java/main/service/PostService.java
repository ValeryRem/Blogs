package main.service;

import main.api.response.MyPostResponce;
import main.api.response.PostAnnounceResponse;
import main.api.response.PostByIdResponce;
import main.api.response.PostsListResponse;
import main.entity.*;
import main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostVoteRepository postVoteRepository;

    public PostService() {
    }

    private List<Post> getPostList() {
        return  postRepository.findAll();
    }

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        List<Post> postList = getPostList();
        List<Post> postListSorted = getSortedPosts(postList, mode);
        List<PostComment> commentList = commentRepository.findAll();
        List<PostAnnounceResponse> posts = new ArrayList<>();
        for (Post post : postListSorted) {
            List<PostComment> commenstByPost = commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).
                    collect(Collectors.toList());
            int commentCountByPost = commenstByPost.size();
            PostAnnounceResponse postAnnounceResponse = new PostAnnounceResponse(post.getPostId(), post.getTime(),
                    post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUser(post));
            postAnnounceResponse.setLikeCount(extractLikeCount(post));
            postAnnounceResponse.setDislikeCount(extractDislikeCount(post));
            posts.add(postAnnounceResponse);
        }
        PostsListResponse postsListResponse = new PostsListResponse(getCount(), posts);
        return getResponseEntity(postsListResponse, offset, limit);
    }

    public ResponseEntity<?> getPostsBySearch(String query, Integer offset, Integer limit, String mode) {
        List<Post> postList = getPostList();
        List<Post> sortedPosts = getSortedPosts(postList, mode);
        List<PostAnnounceResponse> posts = new ArrayList<>();
        List<PostComment> commentList = commentRepository.findAll();
        ResponseEntity<?> responseEntity;
        if (query == null) {
            responseEntity = new ResponseEntity<>("Posts with the query " + query + " not found", HttpStatus.NOT_FOUND);
        } else {
            for (Post post : sortedPosts) {
                if (post.getText().contains(query) && post.getIsActive() && post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                    List<PostComment> commenstByPost = commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).
                            collect(Collectors.toList());
                    int commentCountByPost = commenstByPost.size();
                    posts.add(new PostAnnounceResponse(post.getPostId(), post.getTime(),
                            post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUser(post)));
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
        List<PostComment> commentList = commentRepository.findAll();
        ResponseEntity<?> responseEntity;
        for (Post post : sortedPosts) {
            if (post.getTime().equals(time) && post.getIsActive() &&
                    post.getModerationStatus() == ModerationStatus.ACCEPTED) {
                List<PostComment> commenstByPost = commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).
                        collect(Collectors.toList());
                int commentCountByPost = commenstByPost.size();

                posts.add(new PostAnnounceResponse(post.getPostId(), post.getTime(),
                        post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUser(post)));
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
            List<PostComment> commentList = commentRepository.findAll();
            for (Integer postId : postsId) {
                Post post = postRepository.findById(postId).get();
                List<PostComment> commenstByPost = commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).
                        collect(Collectors.toList());
                int commentCountByPost = commenstByPost.size();
                postsList.add(new PostAnnounceResponse(post.getPostId(), post.getTime(),
                        post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUser(post)));
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
        MyPostResponce myPostResponce;
        int count = 0;
        for (Post post : posts) {
            if (post.getUserId().equals(myUserId))
//                    && (post.getIsActive() == 0
//                    || ((post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.NEW))
//                    || (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.DECLINED))
//                    || (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED)))))
            {
                myPostResponce = new MyPostResponce(post);
                myPostResponce.setUser(getUser(post));
                myPostResponce.setLikeCount(extractLikeCount(post));
                myPostResponce.setDislikeCount(extractDislikeCount(post));
                myPostsList.add(myPostResponce);
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
        try {
            Post post = postRepository.getOne(postId);
            PostByIdResponce postByIdResponce = new PostByIdResponce(post);
            postByIdResponce.setCommentList(getCommentList(postId));
            postByIdResponce.setUser(getUser(post));
            postByIdResponce.setLikeCount(extractLikeCount(post));
            postByIdResponce.setDislikeCount(extractDislikeCount(post));
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
                postList.sort((fp, sp) -> {
                    if (extractLikeCount(fp).equals(extractLikeCount(sp))) return 0;
                    else if (extractLikeCount(fp) < extractLikeCount(sp)) return 1;
                    else return -1;
                });
                break;
            case "early":
                postList.sort(Comparator.comparing(Post::getTime));
                break;
            default:
                postList.sort(Comparator.comparing(Post::getTime).reversed());
        }
        return postList;
    }

//    public Comparator<Post> CompareBiLikeCount = new Comparator<Post>() {
//        @Override
//        public int compare(Post fp, Post sp) {
//            if (extractLikeCount(fp).equals(extractLikeCount(sp))) return 0;
//            else if (extractLikeCount(fp) > extractLikeCount(sp)) return 1;
//            else return -1;
//        }
//    };

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

    private List<TreeMap<String, Object>> getCommentList(Integer postId) {
        List<TreeMap<String, Object>> list = new LinkedList<>();
        TreeMap<String, Object> comments = new TreeMap<>();
        try {
            List<PostComment> postComment  =  commentRepository.findAll();
            List<PostComment> listComments = postComment.stream().filter(a -> (a.getPostId().equals(postId))).collect(Collectors.toList());
            listComments.forEach(a -> {
                TreeMap<String, Object> treeMap = new TreeMap<>();
                treeMap.put("id", a.getCommentId());
                treeMap.put("timestamp", a.getTime());
                treeMap.put("text", a.getText());
                LinkedHashMap<String, Object> map =  new LinkedHashMap<>();
                map.put("id", a.getUserId());
                User user = new User(a.getUserId());
                map.put("name", user.getName());
                map.put("photo", user.getPhoto());
                treeMap.put("user", map);
                comments.putAll(treeMap);
                list.add(comments);
            });
            return list;
        } catch (NullPointerException npe) {
            return list;
        }
    }

    private TreeMap<String, Object>  getUser (Post post){
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("id", post.getPostId());
        try{
            String userName = userRepository.findAll().stream().
                    filter(a->(a.getUserId().equals(post.getUserId()))).
                    findAny().
                    get().
                    getName();
            map.put("name", userName);
            return map;
        }
        catch (Exception ex) {
            return map;
        }
    }

    private Integer extractLikeCount(Post post) {
        try {
            List<PostVote> list = postVoteRepository.findAll();
            List<PostVote> listVotes = list.stream().filter(a -> (a.getPostId().equals(post.getPostId())) && a.getValue() == 1).collect(Collectors.toList());
            return listVotes.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    private Integer extractDislikeCount(Post post) {
        try {
            List<PostVote> list = postVoteRepository.findAll();
            List<PostVote> listVotes = list.stream().filter(a -> (a.getPostId().equals(post.getPostId())) && a.getValue() == -1).collect(Collectors.toList());
            return listVotes.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }
}
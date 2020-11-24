package main.service;

import main.api.response.*;
import main.entity.*;
import main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetService {
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


    private boolean result;

    public GetService() {
    }

    private List<Post> getPostList() {
        return  postRepository.findAll().stream().
                filter(a -> (a.isActive() && ModerationStatus.ACCEPTED.equals(a.getModerationStatus()))).
                collect(Collectors.toList());
    }

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        var postList = getPostList();
        var postListSorted = getSortedPosts(postList, mode);
        var commentList = commentRepository.findAll();
        List<PostAnnounceResponse> responseList = new ArrayList<>();
        for (Post post : postListSorted) {
            int commentCountByPost = (int) commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).count();
            var postAnnounceResponse = new PostAnnounceResponse(post.getPostId(), post.getTime(),
                    post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUser(post));
            postAnnounceResponse.setLikeCount(extractLikeCount(post));
            postAnnounceResponse.setDislikeCount(extractDislikeCount(post));
            responseList.add(postAnnounceResponse);
        }
        var postsListResponse = new PostsListResponse(getCount(), responseList);
        return getResponseEntity(postsListResponse, offset, limit);
    }

    public ResponseEntity<?> getPostsBySearch(String query, Integer offset, Integer limit, String mode) {
        var postList = getPostList();
        var sortedPosts = getSortedPosts(postList, mode);
        var commentList = commentRepository.findAll();
        List<PostAnnounceResponse> responseList;// = new ArrayList<>();
        responseList = sortedPosts.stream().
                filter(p -> p.getText().contains(query)).
                map(p -> {
            int commentCountByPost = (int) commentList.stream().filter(a -> a.getPostId().equals(p.getPostId())).count();
            var postAnnounceResponse =  new PostAnnounceResponse(p.getPostId(), p.getTime(),
                    p.getTitle(), p.getAnnounce(), commentCountByPost, p.getViewCount(), getUser(p));
            postAnnounceResponse.setLikeCount(extractLikeCount(p));
            postAnnounceResponse.setDislikeCount(extractDislikeCount(p));
            return postAnnounceResponse;
        }).
                collect(Collectors.toList());

//        for (Post post : sortedPosts) {
//            if (post.getText().contains(query)) {
//                int commentCountByPost = (int) commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).count();
//                var postAnnounceResponse =  new PostAnnounceResponse(post.getPostId(), post.getTime(),
//                        post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUser(post));
//                postAnnounceResponse.setLikeCount(extractLikeCount(post));
//                postAnnounceResponse.setDislikeCount(extractDislikeCount(post));
//                responseList.add(postAnnounceResponse);
//            }
//        }
        var postsListResponse = new PostsListResponse(responseList.size(), responseList);
        System.out.println("postsByQueryCount: " + responseList.size());// Testing printout
        return  getResponseEntity(postsListResponse, offset, limit);
    }

    public ResponseEntity<?> getPostsByDate(LocalDate time, Integer offset, Integer limit, String mode) {
        var postList = getPostList();
        var sortedPosts = getSortedPosts(postList, mode);
        List<PostAnnounceResponse> posts = new ArrayList<>();
        var commentList = commentRepository.findAll();
        ResponseEntity<?> responseEntity;
        for (Post post : sortedPosts) {
            if (post.getTime().equals(time)) {
                int commentCountByPost = (int) commentList.stream().
                        filter(a -> a.getPostId().equals(post.getPostId())).
                        count();
                posts.add(new PostAnnounceResponse(post.getPostId(), post.getTime(),
                        post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUser(post)));
            }
        }
        if (posts.size() == 0) {
            responseEntity = new ResponseEntity<>("Post with the date " + time + " not found", HttpStatus.NOT_FOUND);
        } else {
            responseEntity = getResponseEntity(new PostsListResponse(posts.size(), posts), offset, limit);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getPostsByTag(String tagName, Integer offset, Integer limit, String mode) {
        ResponseEntity<?> responseEntity;
        try {
            var iterableTags = tagRepository.findAll();
            for (Tag tag : iterableTags) {
                if (tag.getName().equals(tagName)) {
                    tagId = tag.getId();
                    break;
                }
            }
            List<Integer> postsId = new ArrayList<>();
            var tag2PostIterable = tag2PostRepository.findAll();
            for (Tag2Post tag2Post : tag2PostIterable) {
                if (tag2Post.getTagId().equals(tagId)) {
                    postsId.add(tag2Post.getPostId());
                }
            }
            List<Post> posts = new ArrayList<>();
            var sortedPosts = getSortedPosts(posts, mode);
            List<PostAnnounceResponse> postsList = new ArrayList<>();
            var commentList = commentRepository.findAll();
            for (Integer postId : postsId) {
                Post post = postRepository.getOne(postId);
                var commenstByPost = commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).
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
//        getAuthCheck(myUserId);
        if (result) {
            ResponseEntity<?> responseEntity;
            var posts = getPostList();
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
        } else {
            return new ResponseEntity<>("User not authorized", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getPostById(Integer postId) {
        try {
            var post = postRepository.getOne(postId);
            var postByIdResponce = new PostByIdResponce(post);
            postByIdResponce.setCommentList(getCommentList(postId));
            postByIdResponce.setUser(getUser(post));
            postByIdResponce.setLikeCount(extractLikeCount(post));
            postByIdResponce.setDislikeCount(extractDislikeCount(post));
            if (post.getIsActive() && post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
                    post.getTime().compareTo(LocalDate.now()) <= 0) {
                Iterable<Tag2Post> tag2PostIterable = tag2PostRepository.findAll();
                var tagsIdList = new ArrayList<>();
                for (Tag2Post tag2Post : tag2PostIterable) {
                    if (tag2Post.getPostId().equals(postId)) {
                        tagsIdList.add(tag2Post.getTagId()); // формируем лист id тэгов, связанных с postId
                    }
                }
                var iterableTags = tagRepository.findAll();
                for (Tag tag : iterableTags) {
                    if (tagsIdList.contains(tag.getId())) {
                        postByIdResponce.getTags().add(tag); // добавляем тэги в объект вывода
                    }
                }
            }
            return new ResponseEntity<>(postByIdResponce, HttpStatus.FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Post with ID = " + postId + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getPostsForModeration(Integer offset, Integer limit, String mode) {
        var postList = postRepository.findAll();
        var postsFiltered = postList.stream().
                filter(a -> !a.getModerationStatus().equals(ModerationStatus.ACCEPTED) && a.isActive()).
                collect(Collectors.toList());
        if(postsFiltered.size() == 0) {
            return new ResponseEntity<>("No posts for moderation!", HttpStatus.NOT_FOUND);
        } else {
            var postListSorted = getSortedPosts(postsFiltered, mode);
            List<PostComment> commentList = commentRepository.findAll();
            List<PostAnnounceResponse> posts = new ArrayList<>();
            for (Post post : postListSorted) {
                int commentCountByPost = (int) commentList.stream().
                        filter(a -> a.getPostId().equals(post.getPostId())).
                        count();
                var postAnnounceResponse = new PostAnnounceResponse(post.getPostId(), post.getTime(),
                        post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUser(post));
                postAnnounceResponse.setLikeCount(extractLikeCount(post));
                postAnnounceResponse.setDislikeCount(extractDislikeCount(post));
                posts.add(postAnnounceResponse);
            }
            var postsListResponse = new PostsListResponse(getCount(), posts);
            return getResponseEntity(postsListResponse, offset, limit);
        }
    }

    public ResponseEntity<?> getAuthCheck (Integer userId) {
        User u = userRepository.getOne(userId);
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("id", userId);
        map.put("name", u.getName());
        map.put("photo", u.getPhoto());
        map.put("email", u.getEmail());
        map.put("moderation", u.getIsModerator());
        map.put("moderationCount", getModerationCount(u));
        map.put("settings", u.getIsModerator());
        result = u.getIsModerator();

        if (result) {
            var authCheckResponse = new AuthCheckResponse(result, map);
            return new ResponseEntity<>(authCheckResponse, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("result:" + result, HttpStatus.UNAUTHORIZED);
        }
    }

    private Integer getModerationCount (User user) {
        if (user.getIsModerator()) {
            var list = getPostList().stream().
                    filter(a -> (a.getModerationStatus().equals(ModerationStatus.NEW))).
                    collect(Collectors.toList());
            return list.size();
        } else {
            return 0;
        }

    }

    public ResponseEntity<?> getTag (String query) {
        double ratioToCount;
        var count = getCount();
        List<Double> partialWeights = new ArrayList<>();
        List<TreeMap <String, Object>> resultList = new ArrayList<>();
        List<Post> postList = getPostList();
        try {
            String[] tagsSplit = query.split("(?=#)"); //Разбиваем строку запроса на теги с сохранением # в начале слов
            for (String s : tagsSplit) {
                s = s.trim();
                String finalS = s;
                int numberOfPostsWithTag = (int) postList.stream().
                        filter(p -> p.getText().contains(finalS)).
                        count();
                ratioToCount = (double) numberOfPostsWithTag / count;
                partialWeights.add(ratioToCount);
            }

            double maxPartialWeight = partialWeights.stream().max(Comparator.naturalOrder()).get();
            for (int i = 0; i < partialWeights.size(); i++) {
                TreeMap <String, Object> tagsAndWeights = new TreeMap<>();
                tagsAndWeights.put("name", tagsSplit[i]);
                tagsAndWeights.put("weight", partialWeights.get(i) / maxPartialWeight);
                resultList.add(tagsAndWeights);
            }
            return new ResponseEntity<>(resultList, HttpStatus.FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(tagRepository.findAll(), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> getMyStatistics (Integer userId) {
        User user = userRepository.getOne(userId);
        result = user.getIsModerator();
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        ResponseEntity<?> responseEntity;
        if (result) {
            int myPostsCount = (int) postRepository.findAll().stream().
                    filter(a -> a.getUserId().equals(userId)).
                    count();
            map.put("postsCount", myPostsCount);
            int myPostsLikeCount = (int) postVoteRepository.findAll().stream().
                    filter(p -> p.getUserId().equals(userId) && p.getValue() == 1).
                    count();
            map.put("likesCount", myPostsLikeCount);
            int myPostsDislikeCount = (int) postVoteRepository.findAll().stream().
                    filter(p -> p.getUserId().equals(userId) && p.getValue() == -1).
                    count();
            map.put("disLikesCount", myPostsDislikeCount);
            List<Integer> list = postRepository.findAll().stream().
                    map(p -> p.getViewCount()).
                    collect(Collectors.toList());

            int viewMyPostsCount = list.stream().
                    reduce((left, right) -> left + right).
                    get();
            map.put("viewsCount", viewMyPostsCount);
            List<LocalDate> localDates =  postRepository.findAll().stream().
                    map(p -> p.getTime()).collect(Collectors.toList());
            LocalDate minLocalDate = localDates.stream()
                    .min( Comparator.comparing( LocalDate::toEpochDay ))
                    .get();
//            Timestamp timestampMin = Timestamp.valueOf(minLocalDate.atTime(LocalTime.MIDNIGHT));
            map.put("firstPublication", minLocalDate);
            responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("The user is not authorized!", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    private List<Post> getSortedPosts(List<Post> postList, String mode) {
        if ("popular".equals(mode)) {
            postList.sort(Comparator.comparing(Post::getViewCount).reversed());
        } else if ("best".equals(mode)) {
            postList.sort((fp, sp) -> {
                if (extractLikeCount(fp).equals(extractLikeCount(sp))) return 0;
                else if (extractLikeCount(fp) < extractLikeCount(sp)) return 1;
                else return -1;
            });
        } else if ("early".equals(mode)) {
            postList.sort(Comparator.comparing(Post::getTime));
        } else {
            postList.sort(Comparator.comparing(Post::getTime).reversed());
        }
        return postList;
    }

    private ResponseEntity<?> getResponseEntity(PostsListResponse postsListResponse, Integer offset, Integer limit) {
        try {
            ResponseEntity<?> responseEntity;
            List<PostAnnounceResponse> responseListToShow = new ArrayList<>();
            var countOfPosts = postsListResponse.getCount();
            if (limit <= 0) {
                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else if (limit == 1) {
                responseListToShow.add(postsListResponse.getPosts().get(0));
                responseEntity = new ResponseEntity<>(new PostsListResponse(1, responseListToShow), HttpStatus.PARTIAL_CONTENT);
            } else if (limit < countOfPosts) {
                responseListToShow = postsListResponse.getPosts().subList(offset, limit);
                responseEntity = new ResponseEntity<>(new PostsListResponse(postsListResponse.getCount(), responseListToShow), HttpStatus.OK);
            } else {
                responseListToShow = postsListResponse.getPosts().subList(offset, countOfPosts);
                responseEntity = new ResponseEntity<>(new PostsListResponse(postsListResponse.getCount(), responseListToShow), HttpStatus.OK);
            }
            return responseEntity;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Something goes wrong...", HttpStatus.NOT_FOUND);
        }
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
        List<TreeMap<String, Object>> list = new ArrayList<>();
        TreeMap<String, Object> comments = new TreeMap<>();
        try {
            var postComment  =  commentRepository.findAll();
            var listComments = postComment.stream().filter(a -> (a.getPostId().equals(postId))).collect(Collectors.toList());
            listComments.forEach(a -> {
                TreeMap<String, Object> treeMap = new TreeMap<>();
                treeMap.put("id", a.getCommentId());
                treeMap.put("timestamp", a.getTime());
                treeMap.put("text", a.getText());
                LinkedHashMap<String, Object> mapUser =  new LinkedHashMap<>();
                mapUser.put("id", a.getUserId());
                User user = userRepository.findById(a.getUserId()).get();
                mapUser.put("name", user.getName());
                mapUser.put("photo", user.getPhoto());
                treeMap.put("user", mapUser);
                comments.putAll(treeMap);
                list.add(comments);
            });
            return list;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return list;
        }
    }

    private TreeMap<String, Object>  getUser (Post post){
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("id", post.getUserId());
        try{
           User user  = userRepository.getOne(post.getUserId());
           map.put("name", user.getName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    private Integer extractLikeCount(Post post) {
        try {
            var list = postVoteRepository.findAll();
            var listVotes = list.stream().
                    filter(a -> (a.getPostId().equals(post.getPostId())) && a.getValue() == 1).
                    collect(Collectors.toList());
            return listVotes.size();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private Integer extractDislikeCount(Post post) {
        try {
            var list = postVoteRepository.findAll();
            var listVotes = list.stream().filter(a -> (a.getPostId().equals(post.getPostId())) && a.getValue() == -1).
                    collect(Collectors.toList());
            return listVotes.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }
}
package main.service;

import main.api.response.*;
import main.entity.*;
import main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetService {
//    private Integer tagId;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Tag2PostRepository tag2PostRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    HttpSession session;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostVoteRepository postVoteRepository;

    @Autowired
    AuthService authService;

    @Autowired
    GlobalSettingsReporitory globalSettingsReporitory;
//    private final ZoneId zid1 = ZoneId.of("Europe/Moscow");

//    @Autowired
//    PostByIdResponse postByIdResponse;

    private boolean result = false;

    private ResponseEntity<?> responseEntity;
    private GeneralResponse generalResponse;

    public GetService() {
    }

    private List<Post> getActivePosts() {
        return  postRepository.findAll().stream().
                filter(a -> (a.isActive() == 1 && ModerationStatus.ACCEPTED.equals(a.getModerationStatus()))).
                collect(Collectors.toList());
    }

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        generalResponse = new GeneralResponse();
        if (offset > limit) {
            return new ResponseEntity<>("Wrong input parameters!", HttpStatus.BAD_REQUEST);
        }
        var postList = getActivePosts();
        var postListSorted = getPostsFilteredByMode(postList, mode);
        var commentList = commentRepository.findAll();
        List<PostResponse> responseList = new ArrayList<>();
        for (Post post : postListSorted) {
            UserResponse userResponse = new UserResponse();
            int commentCountByPost = (int) commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).count();
            var postResponse = new PostResponse ();
            postResponse.setId(post.getPostId());
            postResponse.setTimestamp(post.getTimestamp().getTime()/1000);
            userResponse.setId(post.getUserId());
            Optional<User> userOptional = userRepository.findById(post.getUserId());
            userOptional.ifPresent(user -> userResponse.setName(user.getName()));
            userOptional.ifPresent(user -> userResponse.setPhoto(user.getPhoto()));
            postResponse.setUserResponse(userResponse);
            postResponse.setTitle(post.getTitle());
            postResponse.setAnnounce(post.getAnnounce());
            postResponse.setCommentCount(commentCountByPost);
            postResponse.setViewCount(post.getViewCount());
            postResponse.setLikeCount(extractLikeCount(post));
            postResponse.setDislikeCount(extractDislikeCount(post));
            responseList.add(postResponse);
        }
        generalResponse.setCount(responseList.size());
        generalResponse.setPosts(getOffsetLimitOutput(responseList, offset, limit));
        responseEntity = new ResponseEntity<>(generalResponse, HttpStatus.OK);
        return responseEntity;
    }

    public ResponseEntity<?> getPostsBySearch(String query, Integer offset, Integer limit, String mode) {
        if (offset > limit) {
            return new ResponseEntity<>("Wrong input parameters!", HttpStatus.BAD_REQUEST);
        }
        generalResponse = new GeneralResponse();
        var postList = getActivePosts();
        var sortedPosts = getPostsFilteredByMode(postList, mode);
        var commentList = commentRepository.findAll();
        List<PostResponse> responseList = new ArrayList<>();
        List<Post> postsList = sortedPosts.stream().
                filter(p -> p.getText().contains(query)).collect(Collectors.toList());
        int count = postsList.size();

//                map(p -> {
//            int commentCountByPost = (int) commentList.stream().filter(a -> a.getPostId().equals(p.getPostId())).count();
//            var postAnnounceResponse =  new PostResponse(p.getPostId(), p.getTimestamp().getTime()/1000,
//                    p.getTitle(), p.getAnnounce(), commentCountByPost, p.getViewCount(), getUserOfPost(p));
//            postAnnounceResponse.setLikeCount(extractLikeCount(p));
//            postAnnounceResponse.setDislikeCount(extractDislikeCount(p));
//            return postAnnounceResponse;
//        }).
//                collect(Collectors.toList());

        postsList.forEach(post -> {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(post.getUserId());
            int commentCountByPost = (int) commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).count();
            Optional<User> userOptional = userRepository.findById(post.getUserId());
            userOptional.ifPresent(user -> userResponse.setName(user.getName()));
            PostResponse postResponse = new PostResponse();
            postResponse.setId(post.getPostId());
            postResponse.setTimestamp(post.getTimestamp().getTime()/1000);
            postResponse.setUserResponse(userResponse);
            postResponse.setTitle(post.getTitle());
            postResponse.setAnnounce(post.getAnnounce());
            postResponse.setLikeCount(extractLikeCount(post));
            postResponse.setDislikeCount(extractDislikeCount(post));
            postResponse.setCommentCount(commentCountByPost);
            postResponse.setViewCount(post.getViewCount());

            responseList.add(postResponse);
        });
        generalResponse.setCount(count);
        generalResponse.setPosts(getOffsetLimitOutput(responseList, offset, limit));
        return  new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> getPostsByDate(LocalDateTime time, Integer offset, Integer limit, String mode) {
        var postList = getActivePosts();
        var sortedPosts = getPostsFilteredByMode(postList, mode);
        List<PostResponse> posts = new ArrayList<>();
        var commentList = commentRepository.findAll();
        for (Post post : sortedPosts) {
            PostResponse postResponse = new PostResponse();
            UserResponse userResponse = new UserResponse();

            if (time.equals(post.getTimestamp().toLocalDateTime()))
//                    .toInstant().atZone(ZoneId.of("UTC")).toLocalDate().equals(time))
            {
                userResponse.setId(post.getUserId());
                Optional<User> userOptional = userRepository.findById(post.getUserId());
                userOptional.ifPresent(user -> userResponse.setName(user.getName()));
                int commentCountByPost = (int) commentList.stream().
                        filter(a -> a.getPostId().equals(post.getPostId())).
                        count();
                postResponse.setId(post.getPostId());
                postResponse.setTimestamp(post.getTimestamp().getTime()/1000);
                postResponse.setTitle(post.getTitle());
                postResponse.setAnnounce(post.getAnnounce());
                postResponse.setLikeCount(extractLikeCount(post));
                postResponse.setDislikeCount(extractDislikeCount(post));
                postResponse.setCommentCount(commentCountByPost);
                postResponse.setViewCount(post.getViewCount());
                postResponse.setUserResponse(userResponse);
                posts.add(postResponse);
            }
        }
        generalResponse = new GeneralResponse();
        generalResponse.setCount(posts.size());
        generalResponse.setPosts(getOffsetLimitOutput(posts, offset, limit));
            responseEntity = new ResponseEntity<>(generalResponse, HttpStatus.OK);
        return responseEntity;
    }

    public ResponseEntity<?> getPostsByTag(String tagName, Integer offset, Integer limit, String mode) {
        var tagList = tagRepository.findAll();
        int tagId;
        List<Integer> postsIdList = new ArrayList<>();
        List<Post> posts;
        List<PostResponse> postsResponseList = new ArrayList<>();
        List<Tag2Post> tag2PostList = tag2PostRepository.findAll();
        if(tagList.size() > 0 && tag2PostList.size() > 0) {
            if (tagList.stream().map(Tag::getTagName).collect(Collectors.toList()).contains(tagName.trim())) {
                tagId = tagList.stream().filter(t -> t.getTagName().equals(tagName)).findFirst().orElse(new Tag()).getId();
                for (Tag2Post tag2Post : tag2PostList) {
                    if (tag2Post.getTagId().equals(tagId)) {
                        postsIdList.add(tag2Post.getPostId());
                    }
                }
                var commentList = commentRepository.findAll();
                posts = postsIdList.stream().map( s -> postRepository.getOne(s)).collect(Collectors.toList());
                var sortedPosts = getPostsFilteredByMode(posts, mode);
                for (Post post: sortedPosts) {
                    PostResponse postResponse = new PostResponse();
                    UserResponse userResponse = new UserResponse();
                    int commentCountByPost = getCommentCountByPost(commentList, post);
                    userResponse.setId(post.getUserId());
                    Optional<User> userOptional = userRepository.findById(post.getUserId());
                    userOptional.ifPresent(user -> userResponse.setName(user.getName()));
                    postResponse.setId(post.getPostId());
                    postResponse.setTimestamp(post.getTimestamp().getTime()/1000);
                    postResponse.setTitle(post.getTitle());
                    postResponse.setAnnounce(post.getAnnounce());
                    postResponse.setLikeCount(extractLikeCount(post));
                    postResponse.setDislikeCount(extractDislikeCount(post));
                    postResponse.setCommentCount(commentCountByPost);
                    postResponse.setViewCount(post.getViewCount());
                    postResponse.setUserResponse(userResponse);
                    postsResponseList.add(postResponse);
//                    postsResponseList.add(new PostResponse(post.getPostId(), post.getTimestamp().getTime() / 1000,
//                            post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUserOfPost(post)));
                }
                generalResponse = new GeneralResponse();
                generalResponse.setCount(postsResponseList.size());
                generalResponse.setPosts(getOffsetLimitOutput(postsResponseList, offset, limit));
                responseEntity = new ResponseEntity<>(generalResponse, HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("Tag " + tagName + " not found!", HttpStatus.NOT_FOUND);
            }
        } else {
            responseEntity = new ResponseEntity<>("No tags or tag2Post yet registered.", HttpStatus.NO_CONTENT);
        }
        return responseEntity;
    }

    private int getCommentCountByPost(List<PostComment> commentList, Post post) {
        var postComments = commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).
                collect(Collectors.toList());
        int commentCountByPost = postComments.size();
        return commentCountByPost;
    }

    public ResponseEntity<?> getMyPosts(Integer offset, Integer limit) {
        if (!authService.isUserAuthorized()) {
            return new ResponseEntity<>("User not authorized", HttpStatus.NOT_FOUND);
        }
        generalResponse = new GeneralResponse();
        int userId = authService.getUserId();
        List<PostResponse> postsResponseList = new ArrayList<>();
        List<Post> posts = postRepository.findAll();//getOffsetLimitOutput(, offset, limit);
            int count = 0;
            for (Post post : posts) {
                if (post.getUserId().equals(userId))
//                    && (post.getIsActive() == 0
//                    || ((post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.NEW))
//                    || (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.DECLINED))
//                    || (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED)))))
                {
                    PostResponse postResponse = new PostResponse();
                    UserResponse userResponse = new UserResponse();
                    var commentList = commentRepository.findAll();
                    int commentCountByPost = getCommentCountByPost(commentList, post);
                    userResponse.setId(post.getUserId());
                    Optional<User> userOptional = userRepository.findById(post.getUserId());
                    userOptional.ifPresent(user -> userResponse.setName(user.getName()));
                    postResponse.setId(post.getPostId());
                    postResponse.setTimestamp(post.getTimestamp().getTime()/1000);
                    postResponse.setTitle(post.getTitle());
                    postResponse.setAnnounce(post.getAnnounce());
                    postResponse.setLikeCount(extractLikeCount(post));
                    postResponse.setDislikeCount(extractDislikeCount(post));
                    postResponse.setCommentCount(commentCountByPost);
                    postResponse.setViewCount(post.getViewCount());
                    postResponse.setUserResponse(userResponse);
                    postsResponseList.add(postResponse);
                    count++;
                }
               generalResponse.setCount(count);
               generalResponse.setPosts(getOffsetLimitOutput(postsResponseList, offset, limit));
               responseEntity = new ResponseEntity<>(generalResponse, HttpStatus.OK);
            }
            return responseEntity;
    }

    public ResponseEntity<?> getPostById(Integer postId) {
        if(postRepository.findById(postId).isPresent()){//findAll().stream().map(Post::getPostId).collect(Collectors.toList()).contains(postId)) {
            var post = postRepository.getOne(postId);
            var postByIdResponse = new PostByIdResponse();
            postByIdResponse.setId(postId);
            postByIdResponse.setTimestamp(post.getTimestamp().getTime()/1000);
            postByIdResponse.setActive(post.isActive() == 1);
            postByIdResponse.setUserResponse(new UserResponse(post));
            postByIdResponse.setTitle(post.getTitle());
            postByIdResponse.setText(post.getText());
            postByIdResponse.setLikeCount(extractLikeCount(post));
            postByIdResponse.setDislikeCount(extractDislikeCount(post));
            postByIdResponse.setViewCount(post.getViewCount() + 1);
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
            if (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
                    post.getTimestamp().getTime() < Timestamp.valueOf(LocalDateTime.now()).getTime()) {
                Iterable<Tag2Post> tag2PostIterable = tag2PostRepository.findAll();
                var tagsIdList = new ArrayList<>();
                ArrayList<String> tags = new ArrayList<>();
                for (Tag2Post tag2Post : tag2PostIterable) {
                    if (tag2Post.getPostId().equals(postId)) {
                        tagsIdList.add(tag2Post.getTagId()); // формируем лист id тэгов, связанных с postId
                    }
                }
//                ArrayList<String> tags = tagsIdList.stream().map((s -> tagRepository.getOne(s).getTagName()).Collect;
                var iterableTags = tagRepository.findAll();
                for (Tag tag : iterableTags) {
                    if (tagsIdList.contains(tag.getId())) {
                       tags.add(tag.getTagName()); // добавляем тэги в объект вывода
                    }
                }
                postByIdResponse.setTags(tags);
                List<CommentsResponse> comments = getCommentsList(postId);
                postByIdResponse.setComments(comments);
            }
            return new ResponseEntity<>(postByIdResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Post with ID = " + postId + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getPostsForModeration(Integer offset, Integer limit, String status) {
        if (!authService.isUserAuthorized()) {
            responseEntity = new ResponseEntity<>("User is UNAUTHORIZED.", HttpStatus.UNAUTHORIZED);
        }
        User us = userRepository.getOne(authService.getUserId());
        UserResponse userResponse = new UserResponse();
//        generalResponse = new GeneralResponse();
        int count;
       List<PostResponse> posts = new ArrayList<>();
        userResponse.setId(us.getUserId());
        userResponse.setName(us.getName());
        var postList = postRepository.findAll();
        var postsFiltered = postList.stream()
                .filter(p -> p.isActive() == 1 && p.getModerationStatus().equals(ModerationStatus.NEW) && status.equals("new"))
                .collect(Collectors.toList());
        List<PostComment> commentList = commentRepository.findAll();
        count = postsFiltered.size();
        for (Post post : postsFiltered) {
            int commentCountByPost = (int) commentList.stream().
                    filter(a -> a.getPostId().equals(post.getPostId())).
                    count();
            PostResponse postResponse = new PostResponse();
            postResponse.setId(post.getPostId());
            postResponse.setTimestamp(post.getTimestamp().getTime()/1000);
            postResponse.setTitle(post.getTitle());
            postResponse.setAnnounce(post.getAnnounce());
            postResponse.setLikeCount(extractLikeCount(post));
            postResponse.setDislikeCount(extractDislikeCount(post));
            postResponse.setCommentCount(commentCountByPost);
            postResponse.setViewCount(post.getViewCount());
            postResponse.setUserResponse(userResponse);
            posts.add(postResponse);
        }
        generalResponse.setCount(count);
        generalResponse.setPosts(getOffsetLimitOutput(posts, offset, limit));
        responseEntity = new ResponseEntity<>(generalResponse, HttpStatus.OK);
        return responseEntity;
    }

    private List<PostResponse> getOffsetLimitOutput (List<PostResponse> list, Integer offset, Integer limit) {
        List<PostResponse> listResult;
        if (offset > list.size()) {
            return new ArrayList<>();
        }
        if (limit + offset <= list.size()) {
            listResult = list.subList(offset, offset + limit);
        } else {
            listResult = list.subList(offset, list.size());
        }
        return listResult;
    }

    public ResponseEntity<?> getTag(String query) {
        Map<String, List<TagResponse>> tagsResponseMap;
        List<String> tagsList;
        if(query.contains(",")) {
            tagsList = List.of(query.split(",")); //Разбиваем строку запроса на теги по запятым
            List<String> tagsCleaned = tagsList.stream().map(String::trim).collect(Collectors.toList());
            tagsResponseMap = getTagResponsesMap(tagsCleaned);
        } else {
            tagsList = List.of(query);
            tagsResponseMap = getTagResponsesMap(tagsList);
        }
        return new ResponseEntity<>(tagsResponseMap, HttpStatus.OK);
    }

    public ResponseEntity<?> getTag() {
        List<Tag> tags = tagRepository.findAll();
        List<String> tagNames = tags.stream().map(Tag::getTagName).collect(Collectors.toList());
        Map<String, List<TagResponse>> tagsMap = getTagResponsesMap(tagNames);
        return new ResponseEntity<>(tagsMap, HttpStatus.OK);
    }

    private Map<String, List<TagResponse>> getTagResponsesMap (List<String> tagNameList) {
        List<Post> postList = getActivePosts();
        var count = getCount();
        List<Integer> postsPerTagList = new ArrayList<>();
        for (String t : tagNameList) {
            postsPerTagList.add((int) postList.stream().filter(p -> p.getText().contains(t)).count());
        }
        int maxPostsPerTag = postsPerTagList.stream().max(Comparator.naturalOrder()).orElse(count);
        List<Double> partialWeights = postsPerTagList.stream().map(t -> (double) t/maxPostsPerTag).collect(Collectors.toList());
        List<TagResponse> tagResponseList = new ArrayList<>();
        for (int i = 0; i < partialWeights.size(); i++) {
            tagResponseList.add(new TagResponse(tagNameList.get(i), partialWeights.get(i)));
        }
        return  Map.of("tags", tagResponseList);

    }

    public ResponseEntity<?> getMyStatistics () {
        Integer myId = authService.getUserId();
        if(authService.isUserAuthorized()) {
            User user = userRepository.getOne(myId);
            result = user.getIsModerator();
            LinkedHashMap<String, Object> map;
            if (result) {
                map = getUserStatistics(myId);
                responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("The user is not moderator!", HttpStatus.OK);
            }
        } else {
            responseEntity = new ResponseEntity<>("The user is not authorized!", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    private LinkedHashMap<String, Object> getUserStatistics(Integer userId) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
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
        List<Integer> list = postRepository.findAllById(Collections.singleton(userId)).stream().
                map(Post::getViewCount).
                collect(Collectors.toList());

        int viewMyPostsCount = list.stream().
                reduce(Integer::sum).
                orElse(0);
        map.put("viewsCount", viewMyPostsCount);
        List<Timestamp> localDates =  postRepository.findAll().stream().filter(p -> p.getUserId().equals(userId)).
//                map(p -> p.getTimestamp().getTime()/1000).collect(Collectors.toList());
        map(Post::getTimestamp).collect(Collectors.toList());
        Timestamp minLocalDate = localDates.stream()
                .min(Comparator.naturalOrder()).get();
        map.put("firstPublication", minLocalDate.getTime()/1000);
        return map;
    }

    /*
    STATISTICS_IS_PUBLIC - если включен этот режим, статистика блога должна быть доступна по запросу GET /api/statistics/all
    для всех групп пользователей. Если режим выключен, по запросу GET /api/statistics/all только модераторам отдавать
    данные статистики. Пользователям и гостям блога необходимо возвращать статус 401.
     */
    public ResponseEntity<?> getAllStatistics () {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("postsCount", getCount());
        int likeCount = (int) postVoteRepository.findAll().stream().filter(p -> p.getValue() == 1).count();
        map.put("likesCount", likeCount);
        int disLikeCount = (int) postVoteRepository.findAll().stream().filter(p -> p.getValue() == -1).count();
        map.put("disLikesCount", disLikeCount);
        List<Integer> list = postRepository.findAll().stream().
                map(Post::getViewCount).
                collect(Collectors.toList());
        int viewCount = list.stream().
                reduce(Integer::sum).orElse(0);
        map.put("viewsCount", viewCount);
        List<Timestamp> localDates =  postRepository.findAll().stream().
                map(Post::getTimestamp).collect(Collectors.toList());
//        List<Long> localDates =  postRepository.findAll().stream().
//                map(p -> p.getTimestamp().getTime()).collect(Collectors.toList());
        Timestamp minLocalDate = localDates.stream()
                .min(Comparator.naturalOrder()).get();
//                .orElse(Timestamp.valueOf(LocalDateTime.now()).getTime()/1000);
        map.put("firstPublication", minLocalDate.getTime()/1000);
        if(globalSettingsReporitory.findAll().stream().
                findAny().
                orElse(new GlobalSettings()).
                isStatisticsIsPublic()) { // if STATISTICS_IS_PUBLIC = true
           responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        } else { // if STATISTICS_IS_PUBLIC = false
            int userId = authService.getUserId();
            User user = userRepository.getOne(userId);
            if (user.getIsModerator()) {
                responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("Access forbidden!", HttpStatus.FORBIDDEN);
            }
        }
        return responseEntity;
    }

    public ResponseEntity<?> getApiCalendar (Integer year) {
        List<Integer> years;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        List<Timestamp> timestamps;
        LinkedHashMap<LocalDate, Integer> posts = new LinkedHashMap<>();
        int postCountAtDate;
        List<Post> postsList = postRepository.findAll();
        postsList.sort(Comparator.comparing(Post::getTimestamp));
        years = postsList.stream().map(p -> convertTimeToYear(p.getTimestamp())).
                distinct().
                collect(Collectors.toList());
        if (year > 1970 && year <= convertTimeToYear(timestamp)) {
            timestamps = postRepository.findAll().stream().
                    map(Post::getTimestamp).
                    filter(t_stamp -> convertTimeToYear(t_stamp).equals(year)).
                    distinct().
                    collect(Collectors.toList());
        } else {
            int currentYear = LocalDate.now().getYear();
            timestamps = postsList.stream().
                    map(Post::getTimestamp).
                    filter(t_stamp -> convertTimeToYear(t_stamp).equals(currentYear)).
                    distinct().
                    collect(Collectors.toList());
        }
        timestamps.sort(Comparator.naturalOrder());
            for (Timestamp d : timestamps) {
                postCountAtDate = (int) postsList.stream().filter(p -> p.getTimestamp().equals(d)).count();
                posts.put(d.toInstant()
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate(), postCountAtDate);
        }
        CalendarResponse calendarResponse = new CalendarResponse(years, posts);
        return new ResponseEntity<>(calendarResponse, HttpStatus.OK);
    }


    private List<Post> getPostsFilteredByMode(List<Post> postList, String mode) {
        if ("popular".equals(mode)) {
            postList.sort(Comparator.comparing(Post::getViewCount).reversed());
        } else if ("best".equals(mode)) {
            postList.sort((fp, sp) -> {
                if (extractLikeCount(fp).equals(extractLikeCount(sp))) return 0;
                else if (extractLikeCount(fp) < extractLikeCount(sp)) return 1;
                else return -1;
            });
        } else if ("early".equals(mode)) {
            postList.sort(Comparator.comparing(Post::getTimestamp));
        } else {
            postList.sort(Comparator.comparing(Post::getTimestamp).reversed());
        }
        return postList;
    }

//    private List<Post> getPostsFilteredByStatus(List<Post> postList, String status) {
//        if(status.equals("new")) {
//           postList = postList.stream()
//                   .filter(p -> p.getModerationStatus().equals(ModerationStatus.NEW))
//                   .collect(Collectors.toList());
//        }
//        if (status.equals("accepted")) {
//            postList = postList.stream()
//                    .filter(p -> p.getModerationStatus().equals(ModerationStatus.ACCEPTED))
//                    .collect(Collectors.toList());
//        }
//        if (status.equals("declined")) {
//            postList = postList.stream()
//                    .filter(p -> p.getModerationStatus().equals(ModerationStatus.DECLINED))
//                    .collect(Collectors.toList());
//        }
//        return postList;
//    }

//    private ResponseEntity<?> getResponseEntity(PostsListResponse postsListResponse, Integer offset, Integer limit) {
//        try {
//            ResponseEntity<?> responseEntity;
//            List<PostAnnounceResponse> responseListToShow = new ArrayList<>();
//            var countOfPosts = postsListResponse.getCount();
//            if (limit <= 0) {
//                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            } else if (limit == 1) {
//                responseListToShow.add(postsListResponse.getPosts().get(0));
//                responseEntity = new ResponseEntity<>(new PostsListResponse(1, responseListToShow), HttpStatus.PARTIAL_CONTENT);
//            } else if (limit < countOfPosts) {
//                responseListToShow = postsListResponse.getPosts().subList(offset, limit);
//                responseEntity = new ResponseEntity<>(new PostsListResponse(postsListResponse.getCount(), responseListToShow), HttpStatus.OK);
//            } else {
//                responseListToShow = postsListResponse.getPosts().subList(offset, countOfPosts);
//                responseEntity = new ResponseEntity<>(new PostsListResponse(postsListResponse.getCount(), responseListToShow), HttpStatus.OK);
//            }
//            return responseEntity;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return new ResponseEntity<>("Something goes wrong...", HttpStatus.NOT_FOUND);
//        }
//    }

    public Integer getCount() {
        int count;
        try {
            List<Post> postList = getActivePosts();
            count = postList.size();
        } catch (NullPointerException ex) {
            count = 0;
        }
        return count;
    }

    private List<CommentsResponse> getCommentsList (Integer postId) {
        List<CommentsResponse> commentsResponseList = new ArrayList<>();
        try {
            var postComment  =  commentRepository.findAll();
            var listComments = postComment.stream().filter(a -> (a.getPostId().equals(postId))).collect(Collectors.toList());
            listComments.forEach(a -> {
                User user = userRepository.findById(a.getUserId()).orElse(new User());
                UserResponse userResponse = new UserResponse(a.getUserId(), user.getName(),  user.getPhoto());
                CommentsResponse commentsResponse = new CommentsResponse(a.getCommentId(), a.getTime(),  a.getText(), userResponse);
                commentsResponseList.add(commentsResponse);
//                comments.setId(a.getCommentId());
//                comments
//                comments.setText( a.getText());
//                TreeMap<String, Object> treeMap = new TreeMap<>();
//                treeMap.put("id", a.getCommentId());
//                treeMap.put("timestamp", a.getTime());
//                treeMap.put("text", a.getText());
//                LinkedHashMap<String, Object> mapUser =  new LinkedHashMap<>();
//                mapUser.put("id", a.getUserId());
//
//                mapUser.put("name", user.getName());
//                mapUser.put("photo", user.getPhoto());
//                treeMap.put("user", mapUser);
//                comments.putAll(treeMap);
//                list.add(comments);com
            });
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        return commentsResponseList;
    }

    private UserResponse getUserOfPost(Post post){
        UserResponse userResponse = new UserResponse();
        TreeMap<String, UserResponse> map = new TreeMap<>();
//        map.put("id", userResponse.getId());
        try{
           User user  = userRepository.getOne(post.getUserId());
           userResponse.setName(user.getName());
           userResponse.setId(user.getUserId());
//           map.put("name", userResponse.getName());
           if(user.getPhoto() != null) {
               userResponse.setPhoto( user.getPhoto());
//               map.put("photo", user.getPhoto());
           }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return map.put("user", userResponse);
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
            var listVotes = list.stream().
                    filter(a -> (a.getPostId().equals(post.getPostId())) && a.getValue() == -1).
                    collect(Collectors.toList());
            return listVotes.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public Integer convertTimeToYear(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        cal.setTimeInMillis(time.getTime());
        String curTime = String.valueOf(cal.get(Calendar.YEAR));
        //String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        return Integer.parseInt(curTime);
    }
}
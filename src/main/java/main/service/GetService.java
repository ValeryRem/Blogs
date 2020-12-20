package main.service;

import main.api.response.*;
import main.entity.*;
import main.repository.*;
import org.apache.commons.io.TaggedIOException;
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
    HttpSession session;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostVoteRepository postVoteRepository;

    @Autowired
    AuthService authService;

    @Autowired
    GlobalSettingsReporitory globalSettingsReporitory;
    private final ZoneId zid1 = ZoneId.of("UTC+6");

    private boolean result = false;

    ResponseEntity<?> responseEntity;

    public GetService() {
    }

    private List<Post> getPostList() {
        return  postRepository.findAll().stream().
                filter(a -> (a.isActive() == 1 && ModerationStatus.ACCEPTED.equals(a.getModerationStatus()))).
                collect(Collectors.toList());
    }

    public ResponseEntity<?> getPosts(Integer offset, Integer limit, String mode) {
        var postList = getPostList();
        var postListSorted = getSortedPosts(postList, mode);
        var commentList = commentRepository.findAll();
        List<PostAnnounceResponse> responseList = new ArrayList<>();
        for (Post post : postListSorted) {
            int commentCountByPost = (int) commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).count();
            var postAnnounceResponse = new PostAnnounceResponse(post.getPostId(), post.getTime().getTime()/1000,
                    post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUserOfPost(post));
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
        List<PostAnnounceResponse> responseList;
        responseList = sortedPosts.stream().
                filter(p -> p.getText().contains(query)).
                map(p -> {
            int commentCountByPost = (int) commentList.stream().filter(a -> a.getPostId().equals(p.getPostId())).count();
            var postAnnounceResponse =  new PostAnnounceResponse(p.getPostId(), p.getTime().getTime()/1000,
                    p.getTitle(), p.getAnnounce(), commentCountByPost, p.getViewCount(), getUserOfPost(p));
            postAnnounceResponse.setLikeCount(extractLikeCount(p));
            postAnnounceResponse.setDislikeCount(extractDislikeCount(p));
            return postAnnounceResponse;
        }).
                collect(Collectors.toList());

        var postsListResponse = new PostsListResponse(responseList.size(), responseList);
        System.out.println("postsByQueryCount: " + responseList.size());// Testing printout
        return  getResponseEntity(postsListResponse, offset, limit);
    }

    public ResponseEntity<?> getPostsByDate(Timestamp time, Integer offset, Integer limit, String mode) {
        var postList = getPostList();
        var sortedPosts = getSortedPosts(postList, mode);
        List<PostAnnounceResponse> posts = new ArrayList<>();
        var commentList = commentRepository.findAll();
        for (Post post : sortedPosts) {
            if (post.getTime().equals(time)) {
                int commentCountByPost = (int) commentList.stream().
                        filter(a -> a.getPostId().equals(post.getPostId())).
                        count();
                posts.add(new PostAnnounceResponse(post.getPostId(), post.getTime().getTime()/1000,
                        post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUserOfPost(post)));
            }
        }
        if (posts.size() == 0) {
            responseEntity = new ResponseEntity<>("Post with the date " + time.toLocalDateTime() + " not found", HttpStatus.NOT_FOUND);
        } else {
            responseEntity = getResponseEntity(new PostsListResponse(posts.size(), posts), offset, limit);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getPostsByTag(String tagName, Integer offset, Integer limit, String mode) {
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
                var postComments = commentList.stream().filter(a -> a.getPostId().equals(post.getPostId())).
                        collect(Collectors.toList());
                int commentCountByPost = postComments.size();
                postsList.add(new PostAnnounceResponse(post.getPostId(), post.getTime().getTime()/1000,
                        post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUserOfPost(post)));
            }
            responseEntity = getResponseEntity(new PostsListResponse(sortedPosts.size(), postsList), offset, limit);
        } catch (Exception ex) {
            responseEntity = new ResponseEntity<>("Tag " + tagName + " not found!", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getMyPosts(Integer myUserId, Integer offset, Integer limit) {
        if (authService.isUserAuthorized()) {
            var posts = getPostList();
            List<MyPostResponse> myPostsList = new ArrayList<>();
            TreeMap<String, Object> map = new TreeMap<>();
            MyPostResponse myPostResponce;
            int count = 0;
            for (Post post : posts) {
                if (post.getUserId().equals(myUserId))
//                    && (post.getIsActive() == 0
//                    || ((post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.NEW))
//                    || (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.DECLINED))
//                    || (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED)))))
                {
                    myPostResponce = new MyPostResponse(post);
                    myPostResponce.setUser(getUserOfPost(post));
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
            var postByIdResponse = new PostByIdResponse(post);
            postByIdResponse.setCommentList(getCommentList(postId));
            postByIdResponse.setUser(getUserOfPost(post));
            postByIdResponse.setLikeCount(extractLikeCount(post));
            postByIdResponse.setDislikeCount(extractDislikeCount(post));
            if (post.getIsActive() == 1 && post.getModerationStatus().equals(ModerationStatus.ACCEPTED) &&
                    post.getTime().getTime() < Timestamp.valueOf(LocalDateTime.now(zid1)).getTime()) {
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
                        postByIdResponse.getTags().add(tag); // добавляем тэги в объект вывода
                    }
                }
            }
            return new ResponseEntity<>(postByIdResponse, HttpStatus.FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Post with ID = " + postId + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getPostsForModeration(Integer offset, Integer limit, String mode) {
        if(authService.isUserAuthorized()) {
            var postList = postRepository.findAll();
            var postsFiltered = postList.stream().
                    filter(a -> !a.getModerationStatus().equals(ModerationStatus.ACCEPTED) && a.isActive() == 1).
                    collect(Collectors.toList());
            if (postsFiltered.size() == 0) {
                return new ResponseEntity<>("No posts for moderation!", HttpStatus.NOT_FOUND);
            } else {
                var postListSorted = getSortedPosts(postsFiltered, mode);
                List<PostComment> commentList = commentRepository.findAll();
                List<PostAnnounceResponse> posts = new ArrayList<>();
                for (Post post : postListSorted) {
                    int commentCountByPost = (int) commentList.stream().
                            filter(a -> a.getPostId().equals(post.getPostId())).
                            count();
                    var postAnnounceResponse = new PostAnnounceResponse(post.getPostId(), post.getTime().getTime()/1000,
                            post.getTitle(), post.getAnnounce(), commentCountByPost, post.getViewCount(), getUserOfPost(post));
                    postAnnounceResponse.setLikeCount(extractLikeCount(post));
                    postAnnounceResponse.setDislikeCount(extractDislikeCount(post));
                    posts.add(postAnnounceResponse);
                }
                var postsListResponse = new PostsListResponse(getCount(), posts);
                return getResponseEntity(postsListResponse, offset, limit);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
        List<String> tagNames = tags.stream().map(Tag::getName).collect(Collectors.toList());
        Map<String, List<TagResponse>> tagsMap = getTagResponsesMap(tagNames);
        return new ResponseEntity<>(tagsMap, HttpStatus.OK);
    }

    private Map<String, List<TagResponse>> getTagResponsesMap (List<String> tagNameList) {
        List<Post> postList = getPostList();
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
                responseEntity = new ResponseEntity<>("The user is not authorized!", HttpStatus.UNAUTHORIZED);
            }
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
        List<Long> localDates =  postRepository.findAll().stream().filter(p -> p.getUserId().equals(userId)).
                map(p -> p.getTime().getTime()).collect(Collectors.toList());
        long minLocalDate = localDates.stream()
                .min(Comparator.naturalOrder())
                .orElse(Timestamp.valueOf(LocalDateTime.now()).getTime()/1000);
        map.put("firstPublication", minLocalDate);
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
        List<Long> localDates =  postRepository.findAll().stream().
                map(p -> p.getTime().getTime()).collect(Collectors.toList());
        long minLocalDate = localDates.stream()
                .min(Comparator.naturalOrder())
                .orElse(Timestamp.valueOf(LocalDateTime.now()).getTime()/1000);
        map.put("firstPublication", minLocalDate);
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

    public ResponseEntity<?> getApiCalendar (Optional<Integer> year) {
        List<Integer> years;
        List<Timestamp> timestamps;
        LinkedHashMap<Long, Integer> posts = new LinkedHashMap<>();
        int postCountAtDate;
        List<Post> postsList = postRepository.findAll();
        postsList.sort(Comparator.comparing(Post::getTime));
        years = postsList.stream().map(p -> convertTimeToYear(p.getTime())).
                distinct().
                collect(Collectors.toList());
        if (year.isPresent()) {
            timestamps = postRepository.findAll().stream().
                    map(Post::getTime).
                    filter(timestamp -> convertTimeToYear(timestamp).equals(year.get())).
                    distinct().
                    collect(Collectors.toList());
        } else {
            int currentYear = LocalDate.now().getYear();
            timestamps = postsList.stream().
                    map(Post::getTime).
                    filter(timestamp -> convertTimeToYear(timestamp).equals(currentYear)).
                    distinct().
                    collect(Collectors.toList());
        }
        timestamps.sort(Comparator.naturalOrder());
            for (Timestamp d : timestamps) {
                postCountAtDate = (int) postsList.stream().filter(p -> p.getTime().equals(d)).count();
                posts.put(d.getTime()/1000, postCountAtDate);
        }
        CalendarResponse calendarResponse = new CalendarResponse(years, posts);
        return new ResponseEntity<>(calendarResponse, HttpStatus.OK);
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
                User user = userRepository.findById(a.getUserId()).orElse(new User());
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

    private TreeMap<String, Object> getUserOfPost(Post post){
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
            var listVotes = list.stream().
                    filter(a -> (a.getPostId().equals(post.getPostId())) && a.getValue() == -1).
                    collect(Collectors.toList());
            return listVotes.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public Integer convertTimeToYear(Timestamp time) {
//        int SEC_IN_YEAR = 86400*365;
//        return  (int) (1 + time/SEC_IN_YEAR);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC+3"));
        cal.setTimeInMillis(time.getTime());
        String curTime = String.valueOf(cal.get(Calendar.YEAR));
        //String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        return Integer.parseInt(curTime);
    }
}
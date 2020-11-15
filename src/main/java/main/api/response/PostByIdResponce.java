package main.api.response;

import main.entity.*;
import main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PostByIdResponce {
    private Integer id;
    private LocalDate timestamp;
    private boolean active;
    private TreeMap<String, Object> user;
    private String title;
    private String text;
//    private String announce;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;
    private List<TreeMap<String, Object>> comments;
    private List<Tag> tags;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    Tag2PostRepository tag2PostRepository;

    @Autowired
    UserRepository userRepository;



    public PostByIdResponce() {
    }

    public PostByIdResponce(Post post) {
        this.id = post.getPostId();
        this.timestamp = post.getTime();
        this.active = true;
//        TreeMap<String, Object> map = new TreeMap<>();
//        map.put("id", post.getPostId());
//        map.put("name", new User(post.getUserId()).getName());
//        this.user = map;
        this.title = post.getTitle();
        this.text = post.getText();
//        this.announce = post.getAnnounce();
        this.viewCount = post.getViewCount();
        setTags(id);
    }

    public void setCommentList(List<TreeMap<String, Object>> commentList) {
        this.comments = commentList;
    }
//        List<TreeMap<String, Object>> list = new ArrayList<>();
//        TreeMap<String, Object> comments = new TreeMap<>();
//        try {
//            List<PostComment> postComment  =  commentRepository.findAll();
//            List<PostComment> listComments = postComment.stream().filter(a -> (a.getPostId().equals(id))).collect(Collectors.toList());
//            listComments.forEach(a -> {
//                TreeMap<String, Object> treeMap = new TreeMap<>();
//                treeMap.put("id", a.getCommentId());
//                treeMap.put("timestamp", a.getTime());
//                treeMap.put("text", a.getText());
//                TreeMap<String, Object> map =  new TreeMap<>();
//                map.put("id", a.getUserId());
//                User user = new User(a.getUserId());
//                treeMap.put("name", user.getName());
//                treeMap.put("photo", user.getPhoto());
//                treeMap.put("user", map);
//                comments.putAll(treeMap);
//                list.add(comments);
//            });
//            this.commentList = list;
//        } catch (NullPointerException npe) {
//            this.commentList = list;
//        }
//    }

    public void setTags(Integer postId) {
        List<Tag> tagList = new ArrayList<>();
        try {
            Iterable<Tag2Post> iterableTags = tag2PostRepository.findAll();
            for (Tag2Post tag2Post : iterableTags) {
                if (tag2Post.getPostId().equals(postId)) {
                    tagList.add(new Tag(tag2Post.getTagId()));
                }
            }
        } catch (NullPointerException ex){
        }
        this.tags = tagList;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public boolean isActive() {
        return active;
    }

    public TreeMap<String, Object> getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

//    public String getAnnounce() {
//        return announce;
//    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Integer getDislikeCount() {
        return dislikeCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public List<Tag> getTags() {
        List<Tag> tagList = new ArrayList<>();
        try {
            Iterable<Tag2Post> iterableTags = tag2PostRepository.findAll();
            for (Tag2Post tag2Post : iterableTags) {
                if (tag2Post.getPostId().equals(id)) {
                    tagList.add(new Tag(tag2Post.getTagId()));
                }
            }
            return  tagList;
        } catch (NullPointerException ex){
            return tagList;
        }
    }

    public List<TreeMap<String, Object>> getComments() {
        return comments;
    }

    public void setUser(TreeMap<String, Object> user) {
        this.user = user;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
}

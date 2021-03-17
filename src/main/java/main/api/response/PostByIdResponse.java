package main.api.response;

import main.entity.*;
import main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

//@Service
public class PostByIdResponse {
    private Integer id;
    private long timestamp;
    private boolean active;
    private TreeMap<String, Object> user;
    private String title;
    private String text;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;
    private List<TreeMap<String, Object>> comments;
    private List<String> tags;

    public PostByIdResponse() {
    }

    public PostByIdResponse(Integer id, long timestamp, boolean active, TreeMap<String, Object> user, String title, String text,
                            Integer likeCount, Integer dislikeCount, Integer viewCount, List<TreeMap<String, Object>> comments,
                            List<String> tags) {
        this.id = id;
        this.timestamp = timestamp;
        this.active = active;
        this.user = user;
        this.title = title;
        this.text = text;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.viewCount = viewCount;
        this.comments = comments;
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public TreeMap<String, Object> getUser() {
        return user;
    }

    public void setUser(TreeMap<String, Object> user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(Integer dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public List<TreeMap<String, Object>> getComments() {
        return comments;
    }

    public void setComments(List<TreeMap<String, Object>> comments) {
        this.comments = comments;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    //    public void setCommentList(List<TreeMap<String, Object>> commentList) {
//        this.comments = commentList;
//    }

//    public void setTags(Integer postId) {
//        List<Tag> tagList = new ArrayList<>();
//        try {
//            Iterable<Tag2Post> iterableTags = tag2PostRepository.findAll();
//            for (Tag2Post tag2Post : iterableTags) {
//                if (tag2Post.getPostId().equals(postId)) {
//                    tagList.add(new Tag(tag2Post.getTagId()));
//                }
//            }
//        } catch (NullPointerException ex){
//            ex.printStackTrace();
//        }
//    }

//    public Integer getId() {
//        return id;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    public boolean isActive() {
//        return active;
//    }
//
//    public TreeMap<String, Object> getUser() {
//        return user;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public String getText() {
//        return text;
//    }
//
////    public String getAnnounce() {
////        return announce;
////    }
//
//    public Integer getLikeCount() {
//        return likeCount;
//    }
//
//    public Integer getDislikeCount() {
//        return dislikeCount;
//    }
//
//    public Integer getViewCount() {
//        return viewCount;
//    }

//    public List<Tag> getTags() {
//        List<Tag> tagList = new ArrayList<>();
//        try {
//            Iterable<Tag2Post> iterableTags = tag2PostRepository.findAll();
//            for (Tag2Post tag2Post : iterableTags) {
//                if (tag2Post.getPostId().equals(id)) {
//                    tagList.add(new Tag(tag2Post.getTagId()));
//                }
//            }
//            return  tagList;
//        } catch (NullPointerException ex){
//            return tagList;
//        }
//    }

//    public List<TreeMap<String, Object>> getComments() {
//        return comments;
//    }
//
//    public void setUser(TreeMap<String, Object> user) {
//        this.user = user;
//    }
//
//    public void setLikeCount(Integer likeCount) {
//        this.likeCount = likeCount;
//    }
//
//    public void setDislikeCount(Integer dislikeCount) {
//        this.dislikeCount = dislikeCount;
//    }
}

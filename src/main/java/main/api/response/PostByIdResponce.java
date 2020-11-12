package main.api.response;

import main.entity.*;
import main.repository.CommentRepository;
import main.repository.PostRepository;
import main.repository.Tag2PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

//@Component
public class PostByIdResponce {
    private Integer id;
    private LocalDate timestamp;
    private boolean active;
    private TreeMap<String, Object> user;
    private String title;
    private String text;
    private String announce;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;
    private List<PostComment> comments;
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
        this.user = post.getUserShort();
        this.title = post.getTitle();
        this.text = post.getText();
        this.announce = post.getAnnounce();
        this.likeCount = post.getLikeCount();
        this.dislikeCount = post.getDislikeCount();
        this.viewCount = post.getViewCount();
        this.comments = getCommentList();
        setTags(id);
    }

    private List<PostComment> getCommentList() {
        List<PostComment> list = new ArrayList<>();
        try {
            commentRepository.findAll().forEach(list::add);
            return list.stream().filter(a -> (a.getPostId().equals(id))).collect(Collectors.toList());
        } catch (NullPointerException npe) {
            return list;
        }
    }

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

    public String getAnnounce() {
        return announce;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Integer getDislikeCount() {
        return dislikeCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public List<PostComment> getComments() {
        return comments;
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
}

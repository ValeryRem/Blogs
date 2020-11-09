package main.api.response;

import main.entity.*;
import main.repository.CommentRepository;
import main.repository.PostRepository;
import main.repository.Tag2PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Component
public class PostByIdResponce {
    private Integer id;
    private LocalDate timestamp;
    private boolean active;
    private User user;
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

    public PostByIdResponce() {
    }

    public PostByIdResponce(Integer id) {
        this.id = id;
        Post post = new Post(id);
        this.timestamp = post.getTime();
        this.active = true;
        this.user = new User(post.getUserId());
        this.title = post.getTitle();
        this.text = post.getText();
        this.announce = post.getAnnounce();
        this.likeCount = post.getLikeCount();
        this.dislikeCount = post.getDislikeCount();
        this.viewCount = post.getViewCount();
        this.comments = getCommentList(id);
        this.tags = getTags(id);
    }

    private List<PostComment> getCommentList(Integer postId) {
        List<PostComment> list = new ArrayList<>();
        commentRepository.findAll().forEach(list::add);
        return list.stream().filter(a -> (a.getPostId().equals(postId))).collect(Collectors.toList());
    }

    private List<Tag> getTags (Integer postId) {
        List<Tag> tagList = new ArrayList<>();
        Iterable<Tag2Post> iterableTags = tag2PostRepository.findAll();
        for(Tag2Post tag2Post: iterableTags) {
            if(tag2Post.getPostId().equals(postId)) {
                tagList.add(new Tag(tag2Post.getTagId()));
            }
        }
        return tagList;
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

    public User getUser() {
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
        return tags;
    }
}

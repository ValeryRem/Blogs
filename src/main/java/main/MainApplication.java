package main;

import main.base.Storage;
import main.model.ModerationStatus;
import main.model.Post;
import main.model.PostComment;
import main.repository.PostRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class MainApplication {
    {
        Post post = new Post("The test post", 1);
        post.setAnnounce("Testing post");
        PostComment comment1 = new PostComment();
        PostComment comment2 = new PostComment();
        comment1.setText("Comment 1");
        comment2.setText("Comment 2");
        List<PostComment> listOfComments = Arrays.asList(comment1, comment2);
        post.setComments(listOfComments);
        post.setDislikeCount(5);
        post.setId(1);
        post.setIsActive(1);
        post.setLikeCount(10);
        post.setModerationStatus(ModerationStatus.ACCEPTED);
        post.setText("This is a testing text");
        post.setTime("2020-10-18");
        post.setUserId(22);
        post.setViewCount(111);
        new Storage().addPost(post);
    }
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}

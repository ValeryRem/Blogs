package main;

import main.base.Storage;
import main.entity.ModerationStatus;
import main.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@SpringBootApplication
public class MainApplication {
    @Autowired
    private final Storage storage;

    public MainApplication(Storage storage) {
        this.storage = storage;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @PostConstruct
    public void init(){
        insertTestPost();
    }

    private void insertTestPost() {
        Post post = new Post();
        post.setTitle("The testing post");
        post.setDislikeCount(5);
        post.setPostId(1);
        post.setIsActive(1);
        post.setLikeCount(10);
        post.setModerationStatus(ModerationStatus.NEW);
        post.setText("This is a testing text");
        post.setTime(LocalDate.now());
        post.setUserId(1);
        post.setViewCount(111);
        storage.addPost(post);
    }
}

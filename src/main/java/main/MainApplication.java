package main;

import main.entity.ModerationStatus;
import main.entity.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@SpringBootApplication
public class MainApplication {
    @Autowired
    private PostRepository postRepository;

    public MainApplication(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @PostConstruct
    public void init(){
        insertTestPost1();
        insertTestPost2();
    }

    private void insertTestPost1() {
        Post post1 = new Post();
        post1.setTitle("The testing post #1");
        post1.setDislikeCount(5);
        post1.setPostId(1);
        post1.setIsActive(1);
        post1.setLikeCount(10);
        post1.setModerationStatus(ModerationStatus.ACCEPTED);
        post1.setText("This is a testing text #1 to be processed by the code into announce.");
        post1.setTime(LocalDate.now());
        post1.setUserId(1);
        post1.setViewCount(111);
        postRepository.save(post1);
    }

    private void insertTestPost2() {
        Post post2 = new Post();
        post2.setTitle("The second testing post");
        post2.setDislikeCount(15);
        post2.setPostId(16);
        post2.setIsActive(1);
        post2.setLikeCount(102);
        post2.setModerationStatus(ModerationStatus.ACCEPTED);
        post2.setText("The second testing text #2 to be processed by the code into announce.");
        post2.setTime(LocalDate.now());
        post2.setUserId(2);
        post2.setViewCount(10);
        postRepository.save(post2);
    }
}

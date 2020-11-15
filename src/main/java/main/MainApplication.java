package main;

import main.entity.ModerationStatus;
import main.entity.Post;
import main.entity.User;
import main.repository.CommentRepository;
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

    @Autowired
    private CommentRepository commentRepository;

    public MainApplication(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
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
        Post post1 = new Post("The first testing post");
        post1.setIsActive(true);
        post1.setModerationStatus(ModerationStatus.ACCEPTED);
        post1.setText("This is a testing text #1 to be processed by the code into announce.");
        post1.setTime(LocalDate.now());
        post1.setUserId(1);
        post1.setViewCount(111);
        post1.setModeratorId(1);
        postRepository.save(post1);
        User user = new User(post1.getUserId());
        user.setName("Ivan");
    }

    private void insertTestPost2() {
        Post post2 = new Post("The second testing post");
        post2.setIsActive(true);
        post2.setModerationStatus(ModerationStatus.ACCEPTED);
        post2.setText("The second testing text #2 to be processed by the code into announce.");
        post2.setTime(LocalDate.of(2020, 11, 2));
        post2.setUserId(2);
        post2.setModeratorId(2);
        post2.setViewCount(10);
        postRepository.save(post2);
    }
}

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
//    @Autowired
//    private final Storage storage;
//    public MainApplication(Storage storage) {
//        this.storage = storage;
//    }
    @Autowired
    private final PostRepository postRepository;

    public MainApplication(PostRepository postRepository) {
        this.postRepository = postRepository;
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
//        String announce = post.getAnnounce();
        post.setTime(LocalDate.now());
        post.setUserId(1);
        post.setViewCount(111);
        postRepository.save(post);
//        storage.addPost(post);
    }
}

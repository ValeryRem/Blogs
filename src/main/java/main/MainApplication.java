package main;

import main.entity.ModerationStatus;
import main.entity.Post;
import main.entity.User;
import main.repository.PostRepository;
import main.repository.UserRepository;
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
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

//    @PostConstruct
//    public void init(){
//        insertTestPost1();
//        insertTestPost2();
//    }

    private void insertTestPost1() {
        Post post1 = new Post("The testing post");
        User user = new User();
        user.setName("Tommy");
        user.setEmail("klo@ggg.ty");
        user.setRegTime(LocalDate.now());
        user.setPassword("pw1212");
        user.setCode("hththt");
        user.setIsModerator(true);
        user.setPhoto("address");
        post1.setIsActive(1);
        post1.setModerationStatus(ModerationStatus.ACCEPTED);
        post1.setText("This is a new testing text to be processed by the code under #Spring, #PHP, #Python tags");
        post1.setTime(LocalDate.now());
        post1.setUserId(1);
        post1.setViewCount(15);
        post1.setModeratorId(2);
        postRepository.save(post1);
        userRepository.save(user);
    }

    private void insertTestPost2() {
        Post post2 = new Post("The testing post");
        post2.setIsActive(1);
        post2.setModerationStatus(ModerationStatus.NEW);
        post2.setText("Another testing text to be processed by the code into announce #Java");
        int day = LocalDate.now().getDayOfMonth();
        int month = LocalDate.now().getMonthValue();
        post2.setTime(LocalDate.of(2018, month, day));
        post2.setUserId(2);
        post2.setModeratorId(1);
        post2.setViewCount(11);
        postRepository.save(post2);
    }
}

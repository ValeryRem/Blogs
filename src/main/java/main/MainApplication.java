package main;

import main.entity.ModerationStatus;
import main.entity.Post;
import main.entity.User;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.TimeZone;

@SpringBootApplication
public class MainApplication {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//        setTimeZone();
//        insertTestPost1();
//        insertTestPost2();
    }

    private void insertTestPost1() {
        Post post1 = new Post("The cute testing post");
        User user = new User();
//        user.setName("Bobby");
//        user.setEmail("lolo@ggg.ty");
//        user.setRegTime(Timestamp.valueOf(LocalDateTime.now()));
//        user.setPassword("pw1234");
//        user.setCode("hthpoi");
//        user.setIsModerator(true);
//        user.setPhoto("address7");
        post1.setIsActive(1);
        post1.setModerationStatus(ModerationStatus.ACCEPTED);
        post1.setText("Absolutely new testing text to be processed by the code under #Spring, #PHP, #Python tags");
        post1.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
//        post1.setUserId(1);
        post1.setViewCount(15);
        post1.setModeratorId(2);
        postRepository.save(post1);
//        userRepository.save(user);
    }

    private void insertTestPost2() {
        Post post2 = new Post("The testing post");
        post2.setIsActive(1);
        post2.setModerationStatus(ModerationStatus.NEW);
        post2.setText("Another testing text to be processed by the code into announce #Java");
        post2.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        post2.setUserId(2);
        post2.setModeratorId(1);
        post2.setViewCount(11);
        postRepository.save(post2);
    }

    private void setTimeZone () {
//        long ts = System.currentTimeMillis();
//        Date localTime = new Date(ts);
        ZoneId zid1 = ZoneId.of("UTC");
        String format = "yyyy.MM.dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat (format);
        sdf.setTimeZone(TimeZone.getTimeZone(zid1));
    }
}

package main.service;

import main.entity.ModerationStatus;
import main.entity.Session;
import main.entity.User;
import main.repository.PostRepository;
import main.repository.SessionRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

@Service
public class PostService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    SessionRepository sessionRepository;

    public ResponseEntity<?> checkAuthLogin (String userEmail, String userPassword) {
        boolean result = false;
        ResponseEntity<?> responseEntity;
        List<User> userList = userRepository.findAll();
        List<Object> resultList = new ArrayList<>();
        LinkedHashMap<String, Object> user = new LinkedHashMap<>();
        int moderationCount;
        User us;
        try {
             us = userList.stream().filter(u -> u.getEmail().equals(userEmail) && u.getPassword().equals(userPassword)).findAny().get();
            result = true;
            resultList.add(result);
            user.put("id", us.getUserId());
            user.put("name", us.getName());
            user.put("photo", us.getPhoto());
            user.put("email", us.getEmail());
            user.put("moderation", "true");
            if (us.getIsModerator()) {
                moderationCount = (int) postRepository.findAll().stream().
                        filter(p -> p.getUserId().equals(us.getUserId()) && p.getModerationStatus().equals(ModerationStatus.NEW)).
                        count();
            } else {
                moderationCount = 0;
            }
            user.put("moderationCount", moderationCount);
            user.put("settings", "true");
            resultList.add(user);
            LocalDate date = LocalDate.now();
            ZoneId zoneId = ZoneId.systemDefault();
            long epoch = date.atStartOfDay().atZone(zoneId).toEpochSecond();
            String sessionTime = epoch + "";
            Session session = new Session(sessionTime, us.getUserId());
            sessionRepository.save(session)
            responseEntity = new ResponseEntity<>(resultList, HttpStatus.OK);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            responseEntity = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }
}

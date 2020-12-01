package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.entity.CaptchaCode;
import main.entity.ModerationStatus;
import main.entity.User;
import main.repository.CaptchaRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthSevice {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;
    Map<String, Integer> sessionMap = new TreeMap<>();

    @Autowired
    HttpSession session;

    @Autowired
    CaptchaRepository captchaRepository;
    boolean result = false;

    public ResponseEntity<?> postAuthLogin(String userEmail, String userPassword) {
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
            String sessionId = session.getId();
//            LocalDate date = LocalDate.now();
//            ZoneId zoneId = ZoneId.systemDefault();
//            long epochSeconds = date.atStartOfDay().atZone(zoneId).toEpochSecond();
//            String sessionTime = epochSeconds + "";
            sessionMap.put(sessionId, us.getUserId());
            responseEntity = new ResponseEntity<>(resultList, HttpStatus.OK);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            responseEntity = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getAuthCheck() {
        boolean result = false;
        Integer userId;
        User u;
        String sessionId = session.getId();
        ResponseEntity<?> responseEntity;
        List<Object> objectList = new ArrayList<>();
        if (!sessionMap.isEmpty()) {
            userId = sessionMap.get(sessionId);
            u = userRepository.getOne(userId);
            TreeMap<String, Object> user = new TreeMap<>();
            user.put("id", userId);
            user.put("name", u.getName());
            user.put("photo", u.getPhoto());
            user.put("email", u.getEmail());
            user.put("moderation", u.getIsModerator());
            user.put("moderationCount", getModerationCount(u));
            user.put("settings", u.getIsModerator());
            result = u.getIsModerator();
            objectList.add(result);
            objectList.add(user);
            responseEntity = new ResponseEntity<>(objectList, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getAuthLogout () {
        String sessionId = session.getId();
        Integer userId = sessionMap.get(sessionId);
        User user = userRepository.getOne(userId);
        if (user.getIsModerator()) {
            sessionMap.clear();
        }
        result = true;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    private Integer getModerationCount(User user) {
        if (user.getIsModerator()) {
            var list = postRepository.findAll().stream().
                    filter(a -> (a.getModerationStatus().equals(ModerationStatus.NEW))).
                    collect(Collectors.toList());
            return list.size();
        } else {
            return 0;
        }
    }

    public ResponseEntity<?> getCaptcha () {
        Cage cage = new GCage();
        String secretCode = cage.getTokenGenerator().next();
        System.out.println("secretCode: " + secretCode);
        String code;
        CaptchaCode captcha = new CaptchaCode();
        Map<String, String> map = new LinkedHashMap<>();
        try (OutputStream os = new FileOutputStream("image.png", false)) {
            cage.draw(cage.getTokenGenerator().next(), os);
            byte[] fileContent = FileUtils.readFileToByteArray(new File("image.png"));
            code = Base64.getEncoder().encodeToString(fileContent);
            captcha.setSecretCode(code);
        } catch (IOException e) {
            e.printStackTrace();
            code = "No captcha.";
        }
        map.put("secret", secretCode);
        map.put("image", "data:image/png;base64, " + code);
        captcha.setCode(code);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}

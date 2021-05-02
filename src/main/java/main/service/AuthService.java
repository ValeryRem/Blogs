package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.requests.LoginRequest;
import main.response.AuthResponse;
import main.response.ResultResponse;
import main.entity.*;
import main.entity.Session;
import main.repository.*;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService{
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final HttpSession httpSession;
    private final CaptchaRepository captchaRepository;
    private final  SessionRepository sessionRepository;
    private final  JavaMailSender javaMailSender;
    private final GlobalSettingsRepository globalSettingsRepository;
    private final LoginRequest loginRequest;

    public AuthService(UserRepository userRepository, PostRepository postRepository,
                       HttpSession httpSession, CaptchaRepository captchaRepository,
                       SessionRepository sessionRepository, JavaMailSender javaMailSender,
                       GlobalSettingsRepository globalSettingsRepository, LoginRequest loginRequest) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.httpSession = httpSession;
        this.captchaRepository = captchaRepository;
        this.sessionRepository = sessionRepository;
        this.javaMailSender = javaMailSender;
        this.globalSettingsRepository = globalSettingsRepository;
        this.loginRequest = loginRequest;
    }

    private boolean result = false;

    public ResponseEntity<?> postAuthLogin(LoginRequest loginRequest) {
        AuthResponse authResponse = new AuthResponse();
        Optional<User> user = userRepository.findOneByEmail(loginRequest.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.ok(new ResultResponse(false));
        }
        User currentUser = user.get();
        if (currentUser.getPassword().equals(loginRequest.getPassword()) ||
                currentUser.getCode().equals(loginRequest.getPassword())) {
            registerSession(currentUser.getUserId()); // put new session id, delete old sessions id
            authResponse.setResult(true);
            LinkedHashMap<String, Object> userResponseMap = getUserResponseMap(currentUser);
            authResponse.setUser(userResponseMap);
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } else
            return ResponseEntity.ok(new ResultResponse(false));
    }

    private LinkedHashMap<String, Object> getUserResponseMap(User currentUser) {
        LinkedHashMap<String, Object> userResponseMap = new LinkedHashMap<>();
        userResponseMap.put("id", currentUser.getUserId());
        userResponseMap.put("name", currentUser.getName());
        userResponseMap.put("photo", currentUser.getPhoto());
        userResponseMap.put("e_mail", currentUser.getEmail());
        userResponseMap.put("moderation", "true");
        userResponseMap.put("moderationCount", getModerationCount(currentUser.getUserId()));
        userResponseMap.put("settings", "true");
        return userResponseMap;
    }

    public void registerSession(Integer userId) {
//        ZoneId zoneId = ZoneId.systemDefault();//.of("UTC"); //
        long epochSeconds = Instant.now().getEpochSecond();//LocalDateTime.now().atZone(zoneId).toEpochSecond();//
        String sessionName = httpSession.getId();
        Timestamp timestamp = new Timestamp(epochSeconds*1000);
        System.out.println(timestamp); // for test only!!!
        Session session = new Session(sessionName, timestamp, userId);
        List<Session> oldSessions = sessionRepository.findAll().stream().
                filter(s -> (int) s.getTimestamp().getTime() / 1000 < (int) epochSeconds - 1800).
                collect(Collectors.toList());
        for (Session s : oldSessions) {
            sessionRepository.delete(s);
        }
        if (!sessionRepository.findAll().stream().
                map(Session::getSessionName).
                collect(Collectors.toList()).contains(sessionName)) {
            sessionRepository.save(session);
        }
    }

    public ResponseEntity<?> getAuthCheck() {
        AuthResponse authResponse = new AuthResponse();
        Optional<Integer> userIdOptional;
        User u;
        String sessionName = httpSession.getId();
        boolean isSession = sessionRepository.findAll().stream().
                anyMatch(s -> s.getSessionName().equals(sessionName));
        if (isSession) {
            userIdOptional = sessionRepository.findAll().stream().
                    filter(s -> s.getSessionName().equals(sessionName)).
                    map(Session::getUserId).
                    findAny();
            if (userIdOptional.isPresent()) {
                int userId = userIdOptional.get();
                u = userRepository.getOne(userId);
                LinkedHashMap<String, Object> user = new LinkedHashMap<>();
                user.put("id", userIdOptional.get());
                user.put("name", u.getName());
                user.put("photo", u.getPhoto());
                user.put("email", u.getEmail());
                user.put("moderation", u.getIsModerator());
                user.put("moderationCount", getModerationCount(userId));
                user.put("settings", u.getIsModerator());
                authResponse.setResult(true);
                authResponse.setUser(user);
                return    new ResponseEntity<>(authResponse, HttpStatus.OK);
            } else {
                return  new ResponseEntity<>("User is not authorized.", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return  new ResponseEntity<>("Session is not open.", HttpStatus.OK);
        }
    }

    public ResponseEntity<?> getAuthLogout () {
        ResponseEntity<?> responseEntity;
       if(isUserAuthorized()) {
           String sessionName = httpSession.getId();
           Session session = sessionRepository.findAll().stream().
                   filter(s -> s.getSessionName().equals(sessionName)).
                   findFirst().
                   orElse(new Session(sessionName, new Timestamp(Instant.now().getEpochSecond()), getUserId()));
           sessionRepository.delete(session);
           responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
       } else {
           responseEntity = new ResponseEntity<>(true, HttpStatus.UNAUTHORIZED);
       }
        return responseEntity;
    }

    public ResponseEntity<?> getCaptcha () {
        Cage cage = new GCage();
        String secretCode = cage.getTokenGenerator().next();
        System.out.println("secretCode: " + secretCode);
        String code = cage.getTokenGenerator().next();
        String code64 = "";
        CaptchaCode captcha = new CaptchaCode();
        Map<String, String> map = new LinkedHashMap<>();
        try (OutputStream os = new FileOutputStream("image.png", false)) {
            cage.draw(code, os);
            //resize image
//            BufferedImage bi = cage.drawImage("image.png");
//            resizeImageWithHint(bi, 100, 35, BufferedImage.TYPE_INT_RGB);
//            cage.draw("image.png", new FileOutputStream(String.valueOf(bi)));
            /////
            byte[] fileContent = FileUtils.readFileToByteArray(new File("image.png"));
            code64 = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        captcha.setCode(code);
        captcha.setSecretCode(secretCode);
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        captcha.setTimestamp(timestamp);
        captchaRepository.save(captcha);
        map.put("secret", secretCode);
        map.put("image", "data:image/png;base64, " + code64);
        clearOldCaptchas();
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public ResponseEntity<?> postAuthRegister(LoginRequest loginRequest){
        Map<String, Object>  output = new LinkedHashMap<>();
        ResponseEntity<?> responseEntity;
        if (globalSettingsRepository.findAll().stream().
                findAny().orElse(new GlobalSettings()).
                isMultiuserMode()) { // if MULTIUSER_MODE = true
            User user = new User();
            Map<String, String> errors = new LinkedHashMap<>();
            Optional<User> userOptional = userRepository.findOneByEmail(loginRequest.getEmail());

            result = true;
            if (userOptional.isPresent()) {
                errors.put("email", "Этот e-mail уже зарегистрирован!");
                result = false;
            }
            if (loginRequest.getName().length() > 30) {
                errors.put("name", "Ошибка: длина имени превышает 30 знаков!");
                result = false;
            }
            if (loginRequest.getPassword().length() < 6 || loginRequest.getPassword().length() > 12) {
                errors.put("password", "Пароль имеет недопустимую длину!");
                result = false;
            }

            Optional<CaptchaCode> codeOptional = captchaRepository.findBySecretCode(loginRequest.getCaptchaSecret());
            if(codeOptional.isPresent()) {
                if(!loginRequest.getCaptcha().equals(codeOptional.get().getCode())) {
                    errors.put("captcha", "Код с картинки введён неверно!");
                    result = false;
                }
            }
            if (result) {
                String code = generateCode(16);
                user.setEmail(loginRequest.getEmail());
                user.setName(loginRequest.getName());
                user.setPassword(loginRequest.getPassword());
                user.setRegTime(Timestamp.valueOf(LocalDateTime.now()));
                user.setCode(code);
                user.setIsModerator(false);
                userRepository.save(user);
                output.put("result", true);
                responseEntity = new ResponseEntity<>(output, HttpStatus.OK);
            } else {
                output.put("result", false);
                output.put("errors", errors);
                responseEntity = new ResponseEntity<>(output, HttpStatus.BAD_REQUEST);
            }
        } else { // if MULTIUSER_MODE = false
            responseEntity = new ResponseEntity<>("New users forbidden", HttpStatus.NOT_ACCEPTABLE);
        }
        return responseEntity;
    }

    public ResponseEntity<?> authPassword (String code, String password, String captcha, String captchaSecret) {
        User user = userRepository.getOne(getUserId());
        ResponseEntity<?> responseEntity;
        if(user.getCode().equals(code) && captcha.equals(captchaRepository.findAll().stream().findAny().orElse(new CaptchaCode()).getCode())
        && captchaSecret.equals(captchaRepository.findAll().stream().findAny().orElse(new CaptchaCode()).getSecretCode())) {
            user.setPassword(password);
            userRepository.save(user);
            responseEntity = new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(new ResultResponse(false), HttpStatus.NOT_ACCEPTABLE);
        }
        return responseEntity;
    }

    public ResponseEntity<?> authRestore(String email) {
//        Optional<User> optionalUser = userRepository.findAll().stream().filter(u -> u.getEmail().equals(email)).findAny();
        Optional<User> optionalUser = userRepository.findOneByEmail(email);
        if (optionalUser.isPresent())
        {
            String code = generateCode(16);
            String text = "/login/change-password/" + code;
            System.out.println("code: " + code);
            User user = optionalUser.get();
            user.setCode(code);
            userRepository.save(user);
            try {
                sendEmail(email, "Restore password", text);
            } catch (MailSendException ex) {
                ex.printStackTrace();
                return new ResponseEntity<>(new ResultResponse(false), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
        }
        System.out.println("USER NOT_FOUND");
        return new ResponseEntity<>(new ResultResponse(false), HttpStatus.NOT_FOUND);
    }

    public boolean isUserAuthorized () {
        try {
            String sessName = httpSession.getId();
            List<Session> currentSessions = sessionRepository.findAll();
            result = currentSessions.stream().anyMatch(s -> s.getSessionName().equals(sessName));
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

    private int getModerationCount(Integer userId) {
        int moderCount = 0;
        List<Post> posts = postRepository.findAll();
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getIsModerator() && posts.stream()
                    .anyMatch(p -> p.getModerationStatus().equals(ModerationStatus.NEW))) {
                moderCount = (int) posts.stream().
                        filter(p -> p.getModeratorId().equals(userId) && p.getModerationStatus().
                                equals(ModerationStatus.NEW)).
                        count();
            }
        }
        return moderCount;
    }

    private void sendEmail(String email, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("remenyuk.valery@yandex.ru");
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(text);
        javaMailSender.send(msg);
    }

    public Integer getUserId() {
        return
                sessionRepository.findAll().stream().
                        filter(s -> s.getSessionName().equals(httpSession.getId())).
                        map(Session::getUserId).
                        findAny().orElse(0);
    }

    public User getCurrentUser() {
        return
                userRepository.getOne(getUserId());
    }

    private String generateCode(int length) {
        final String pattern = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(pattern.charAt(rnd.nextInt(pattern.length())));
        }
        return stringBuilder.toString();
    }

    private void clearOldCaptchas() {
        List<CaptchaCode> oldCaptchas = captchaRepository.findAll().stream()
                .filter(c -> c.getTimestamp().getTime() < (Timestamp.valueOf(LocalDateTime.now()).getTime() - 3_600_000))
                .collect(Collectors.toList());
        captchaRepository.deleteInBatch(oldCaptchas);
    }

}

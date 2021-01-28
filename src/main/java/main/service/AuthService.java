package main.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.response.AuthResponse;
import main.api.response.ErrorsResponse;
import main.entity.*;
import main.entity.Session;
import main.repository.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    CaptchaRepository captchaRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private GlobalSettingsReporitory globalSettingsReporitory;

//    private AuthResponse authResponse;
//    private final ZoneId zid1 = ZoneId.of("Europe/Moscow");
    private boolean result = false;
    private ResponseEntity<?> responseEntity;

    public ResponseEntity<?> postAuthLogin(String eMail, String userPassword) {
        AuthResponse authResponse = new AuthResponse();
        Optional<User> user = userRepository.findByEmail(eMail);
        if (user.isEmpty()) {
            return ResponseEntity.ok(new ErrorsResponse());
        }
        User currentUser = user.get();
        if (!currentUser.getPassword().equals(userPassword)) {
            return ResponseEntity.ok(new ErrorsResponse());
        }
        registerSession(currentUser.getUserId()); // put new session id, delete old sessions id
        authResponse.setResult(true);
        LinkedHashMap<String, Object> userResponseMap = getUserResponseMap(currentUser);
        authResponse.setUser(userResponseMap);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private LinkedHashMap<String, Object> getUserResponseMap(User currentUser) {
        LinkedHashMap<String, Object> userResponseMap = new LinkedHashMap<>();
        userResponseMap.put("id", currentUser.getUserId());
        userResponseMap.put("name", currentUser.getName());
        userResponseMap.put("photo", currentUser.getPhoto());
        userResponseMap.put("e_mail", currentUser.getEmail());
        userResponseMap.put("moderation", "true");
        userResponseMap.put("moderationCount", getModerationCount(currentUser));
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
                filter(s -> (int) s.getTime().getTime() / 1000 < (int) epochSeconds - 1800).
                collect(Collectors.toList());
        for (Session s : oldSessions) {
            sessionRepository.delete(s);
        }
        if (!sessionRepository.findAll().stream().
                map(Session::getSessionName).
                collect(Collectors.toList()).contains(httpSession.getId())) {
            sessionRepository.save(session);
        }
    }

    public ResponseEntity<?> getAuthCheck() {
        AuthResponse authResponse = new AuthResponse();
        Integer userId;
        User u;
        String sessionId = httpSession.getId();
        boolean isSession = sessionRepository.findAll().stream().
                anyMatch(s -> s.getSessionName().equals(sessionId));
        if (isSession) {
            userId = sessionRepository.findAll().stream().
                    filter(s -> s.getSessionName().equals(sessionId)).
                    map(Session::getUserId).
                    findAny().
                    orElse(0);
            if (userId > 0) {
                u = userRepository.getOne(userId);
                LinkedHashMap<String, Object> user = new LinkedHashMap<>();
                user.put("id", userId);
                user.put("name", u.getName());
                user.put("photo", u.getPhoto());
                user.put("e_mail", u.getEmail());
                user.put("moderation", u.getIsModerator());
                user.put("moderationCount", getModerationCount(u));
                user.put("settings", u.getIsModerator());
                authResponse.setResult(true);
                authResponse.setUser(user);
                responseEntity = new ResponseEntity<>(authResponse, HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("User is not authorized.", HttpStatus.UNAUTHORIZED);
            }
        }
        return
                responseEntity;
    }

    public ResponseEntity<?> getAuthLogout () {
       if(isUserAuthorized()) {
           String sessionName = httpSession.getId();
           Session session = sessionRepository.findAll().stream().
                   filter(s -> s.getSessionName().equals(sessionName)).
                   findFirst().
                   orElse(new Session(sessionName, new Timestamp(Instant.now().getEpochSecond()), getUserId()));
           sessionRepository.delete(session);
           responseEntity = new ResponseEntity<>("result: true", HttpStatus.OK);
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
        captcha.setSecretCode(secretCode);
        captcha.setCode(code);
        Timestamp time = Timestamp.valueOf(LocalDateTime.now());
        captcha.setTime(time);
        captchaRepository.save(captcha);
        map.put("secret", secretCode);
        map.put("image", "data:image/png;base64, " + code64);
        List<CaptchaCode> captchasOld =  captchaRepository.findAll().stream().
                filter(c -> c.getTime().getTime()/1000 < (time.getTime()/1000 - 3600)).
                collect(Collectors.toList());
        for (CaptchaCode c: captchasOld) {
            captchaRepository.delete(c);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /*
    MULTIUSER_MODE — если включен этот режим, в блоге разрешена регистрация новых пользователей. Если режим выключен,
    регистрация пользователей не доступна, на фронте на месте ссылки на страницу регистрации появляется текст
    Регистрация закрыта. При запросе на /api/auth/register необходимо возвращать статус 404 (NOT FOUND).
     */
    public ResponseEntity<?> postAuthRegister(String eMail, String password, String nameString, String captcha) {
        Map<String, Object>  output = new LinkedHashMap<>();
        if (globalSettingsReporitory.findAll().stream().
                findAny().orElse(new GlobalSettings()).
                isMultiuserMode()) { // if MULTIUSER_MODE = true
            User user = new User();
            LinkedHashMap<String, Object> errors = new LinkedHashMap<>();
            List<User> users = userRepository.findAll();

            result = true;
            if (users.stream().map(User::getEmail).collect(Collectors.toList()).contains(eMail)) {
                errors.put("email", "Этот e-mail уже зарегистрирован!");
                result = false;
            }
            if (users.stream().map(User::getName).collect(Collectors.toList()).contains(nameString)) {
                errors.put("name", "Данное имя уже зарегистрировано!");
                result = false;
            }
            if (password.length() < 6) {
                errors.put("password", "Пароль короче 6 символов!");
                result = false;
            }
            if (!captchaRepository.findAll().stream().map(CaptchaCode::getCode).collect(Collectors.toList()).contains(captcha)) {
                errors.put("captcha", "Код с картинки введён неверно");
                result = false;
            }
            if (result) {
                user.setEmail(eMail);
                user.setName(nameString);
                user.setPassword(password);
                user.setRegTime(Timestamp.valueOf(LocalDateTime.now()));
                user.setCode(captcha);
                userRepository.save(user);
                output.put("result", true);
                responseEntity = new ResponseEntity<>(output, HttpStatus.OK);
            } else {
                output.put("result", false);
                output.put("errors", errors);
                responseEntity = new ResponseEntity<>(output, HttpStatus.NOT_ACCEPTABLE);
            }
        } else { // if MULTIUSER_MODE = false
            responseEntity = new ResponseEntity<>("New users forbidden", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    public ResponseEntity<?> authPassword (String code, String password, String captcha, String captchaSecret) {
        User user = userRepository.getOne(getUserId());
        if(user.getCode().equals(code) && captcha.equals(captchaRepository.findAll().stream().findAny().orElse(new CaptchaCode()).getCode())
        && captchaSecret.equals(captchaRepository.findAll().stream().findAny().orElse(new CaptchaCode()).getSecretCode())) {
            user.setPassword(password);
            userRepository.save(user);
            responseEntity = new ResponseEntity<>("result: true", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("result: false", HttpStatus.NOT_ACCEPTABLE);
        }
        return responseEntity;
    }

    public ResponseEntity<?> authRestore (String eMail) {
        if (userRepository.findAll().stream().
                map(User::getEmail).
                collect(Collectors.toList()).
                contains(eMail)) {
            result = true;
            String code = generateCode(16);
            String text = "/login/change-password/" + code;
            User user = userRepository.findAll().stream().
                    filter(u -> u.getEmail().
                            equals(eMail)).
                    findAny().
                    orElse(new User());
            user.setCode(code);
            userRepository.save(user);
            try {
                sendEmail(eMail, "Restore password", text);
            } catch (MailSendException ex) {
                ex.printStackTrace();
                Map<Object, Exception> failedMails = ex.getFailedMessages();
                System.out.println(failedMails.entrySet());
                return new ResponseEntity<>(failedMails, HttpStatus.BAD_REQUEST);
            }
            responseEntity = new ResponseEntity<>("result: " + result, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("result: " + false, HttpStatus.NOT_FOUND);
        }
        return responseEntity;
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

    private int getModerationCount(User user) {
        int moderCount = 0;
        List<Post> posts = postRepository.findAll();
        if (user.getIsModerator() && posts.stream().map(Post::getModerationStatus)
                .collect(Collectors.toList())
                .contains(ModerationStatus.NEW)) {
            moderCount = (int) posts.stream().
                    filter(p -> p.getUserId().equals(user.getUserId()) && p.getModerationStatus().
                            equals(ModerationStatus.NEW)).
                    count();
        }
        return moderCount;
    }

    private void sendEmail(String eMail, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(eMail);
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

    private String generateCode(int length) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return stringBuilder.toString();
    }

//    private static BufferedImage resizeImageWithHint(BufferedImage originalImage,
//                                                     int IMG__WIDTH, int IMG__HEIGHT, int type){
//
//        BufferedImage resizedImage = new BufferedImage(IMG__WIDTH, IMG__HEIGHT, type);
//        Graphics2D g = resizedImage.createGraphics();
//        g.drawImage(originalImage, 0, 0, IMG__WIDTH, IMG__HEIGHT, null);
//        g.dispose();
//        g.setComposite(AlphaComposite.Src);
//
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        g.setRenderingHint(RenderingHints.KEY_RENDERING,
//                RenderingHints.VALUE_RENDER_QUALITY);
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//
//        return resizedImage;
//    }
}

package main.service;

import main.api.response.ErrorsResponse;
import main.entity.User;
import main.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private AuthService authService;

    @Autowired
    ErrorsResponse errorsResponse;

    @Autowired
    UserRepository userRepository;

    private ResponseEntity<?> responseEntity;
    private boolean result;
    private final Integer PW_MIN_LENGTH = 6;
    private final Integer PW_MAX_LENGTH = 30;


    public ResponseEntity<?> postApiImage(@RequestPart("image") MultipartFile image) throws IOException {
        if (authService.isUserAuthorized()) {

//            FileInputStream input = new FileInputStream(file);
//            MultipartFile multipartFile = new MockMultipartFile("image", imageUri, "image/jpeg",
//                    IOUtils.toByteArray(input));
//            image.transferTo(getOutputFile());
            File convertFile = getOutputFile();
            convertFile.createNewFile();
            try(FileOutputStream fout = new FileOutputStream(convertFile)) {
                fout.write(image.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            responseEntity = new ResponseEntity<>(getOutputFile().getAbsolutePath(), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    public ResponseEntity<?> postApiProfileMy(MultipartFile avatar, String emailMP, String nameMP,
                                              String passwordMP, Integer removePhotoMP) throws IOException {

        if(authService.isUserAuthorized()) {
            result = true;
            User user = userRepository.getOne(authService.getUserId());
            Map<String, Object> errors = new LinkedHashMap<>();
            if(passwordMP.length() >= PW_MIN_LENGTH && passwordMP.length() <= PW_MAX_LENGTH) {
                user.setPassword(passwordMP);
            } else {
                result = false;
                errors.put("password",  "Длина пароля с ошибкой.");
            }
            if(avatar.getBytes().length < 5_000_000) {
                if(removePhotoMP == 1) {
                    user.setPhoto("");
                }
            } else {
                result = false;
                errors.put("photo", "Фото слишком большое, нужно не более 5 Мб.");
            }
            if(user.getEmail().equals(emailMP)) {
                result = false;
                errors.put("e-mail", "Этот e-mail уже зарегистрирован.");
            }
            if(!nameMP.matches("[a-zA-Z]*") || nameMP.length() > 100) {
                result = false;
                errors.put("name", "Имя указано неверно.");
            }
            if (!result) {
                errorsResponse = new ErrorsResponse(false, errors);
                return new ResponseEntity<>(errorsResponse, HttpStatus.BAD_REQUEST);
            } else {
                user.setName(nameMP);
                user.setEmail(emailMP);
                userRepository.save(user);
                return new ResponseEntity<>("result: true", HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
    }

    private File getOutputFile () {
        String targetFolder = "C:\\Users\\valery\\Desktop\\java_basics\\16_Blogs\\upload";
        String destination = StringUtils.cleanPath(targetFolder);
        String hashCode = String.valueOf(Math.abs(targetFolder.hashCode()));
        String folder1 = hashCode.substring(0, hashCode.length() / 3);
        String folder2 = hashCode.substring(1 + hashCode.length() / 3, 2 * hashCode.length() / 3);
        String folder3 = hashCode.substring(1 + 2 * hashCode.length() / 3);
        int suffix = (int) (Math.random() * 100);
        File destFolder = new File(destination);
        if (!destFolder.exists()) {
            destFolder.mkdir();
        }
        File destFolder1 = new File(destination + folder1);
        if (!destFolder1.exists()) {
            destFolder1.mkdir();
        }
        File destFolder2 = new File(destination + folder1 + "/" + folder2);
        if (!destFolder2.exists()) {
            destFolder2.mkdir();
        }
        String finalDestination = destination + folder1 + "/" + folder2 + "/" + folder3 + "/";
        File destFolder3 = new File(finalDestination);
        if (!destFolder3.exists()) {
            destFolder3.mkdir();
        }
        String fileName = suffix + "_uploaded.jpg";
        return new File(destFolder3, fileName);
    }
}

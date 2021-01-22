package main.service;

import main.api.response.AuthResponse;
import main.api.response.ErrorsResponse;
import main.entity.User;
import main.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
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

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
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


    public ResponseEntity<?> postApiImage(MultipartFile image) throws IOException {
        User user = userRepository.getOne(authService.getUserId());
        if (authService.isUserAuthorized()) {
            String imageAddress = getImageAddress(image);
            System.out.println(imageAddress); // test
            user.setPhoto(imageAddress);
            userRepository.save(user);
            responseEntity = new ResponseEntity<>(imageAddress, HttpStatus.OK);
        } else {
            responseEntity = ResponseEntity.ok(new ErrorsResponse());//("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    @NotNull
    private String getImageAddress(MultipartFile image) {
        File convertFile = getOutputFile(image);
//            convertFile.createNewFile();
        try(FileOutputStream fout = new FileOutputStream(convertFile)) {
            fout.write(image.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertFile.getAbsolutePath();
    }

    public ResponseEntity<?> getPostProfileMy(MultipartFile photo, String email, String name,
                                              String password, String removePhoto) throws IOException {
        if (!authService.isUserAuthorized()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        result = true;
        User currentUser = userRepository.getOne(authService.getUserId());
        Map<String, Object> errors = new LinkedHashMap<>();
        int width, height;
        if(photo != null) {
            if (photo.getBytes().length <= 5_000_000) {
                if (removePhoto.equals("1")) {
                    currentUser.setPhoto("");
                } else {
                    String photoDestination = getImageAddress(photo);
                    Image image = ImageIO.read(photo.getInputStream());
                    width = image.getWidth(null);
                    height = image.getHeight(null);
                    if (width > 30 || height > 30) {
//                        String avatarSrc = photo.getOriginalFilename();
                        File newFilePng = new File(photoDestination);
                        BufferedImage tempPNG = resizeImage(image, 30, 30);
                        ImageIO.write(tempPNG, "png", newFilePng);
                        String photoUrl = newFilePng.getAbsolutePath();
//                        photoUrl.replace("\\", "/");
                        currentUser.setPhoto(photoUrl);
                        System.out.println("avatarAddress: " + photoDestination);//((ImageOutputStream) image).readLine());
                    } else {
                        currentUser.setPhoto(photoDestination);
                    }
                }
            } else {
                result = false;
                errors.put("photo", "Фото слишком большое, нужно не более 5 Мб.");
            }
        }
        if (password != null) {
            if (password.length() < PW_MIN_LENGTH && password.length() > PW_MAX_LENGTH) {
                result = false;
                errors.put("password", "Длина пароля с ошибкой");
            }
        }
        if (!name.matches("[a-zA-Z]*") || name.length() > 100 || name.length() < 2) {
            result = false;
            errors.put("name", "Имя указано неверно.");
        }
        if (!result) {
            errorsResponse = new ErrorsResponse(false, errors);
            return new ResponseEntity<>(errorsResponse, HttpStatus.BAD_REQUEST);
        } else {
            currentUser.setName(name);
            if(email != null) {
                currentUser.setEmail(email);
            }
            if(password != null) {
                currentUser.setPassword(password);
            }
            userRepository.save(currentUser);
            return new ResponseEntity<>("result: true", HttpStatus.OK);
        }
    }

    private File getOutputFile (MultipartFile image) {
        String targetFolder = "C:/Users/valery/Desktop/java_basics/16_Blogs/upload/";
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
        File destFile = new File(finalDestination);
        if (!destFile.exists()) {
            destFile.mkdir();
        }
        String fileName = suffix + "_"+  image.getOriginalFilename();//.getName();
        return new File(destFile, fileName);
    }

    public static BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        //below three lines are for RenderingHints for better image quality at cost of higher processing time
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
//        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }

}

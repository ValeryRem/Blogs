package main.service;

import main.requests.ProfileRequest;
import main.response.ResultResponse;
import main.entity.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class UserService {
    private final AuthService authService;
    private final  UserRepository userRepository;

    public UserService(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    private final ResultResponse resultResponse = new ResultResponse(false);

    public ResponseEntity<?> postApiImage(MultipartFile image) throws IOException {
        User user = userRepository.getOne(authService.getUserId());
        if (authService.isUserAuthorized()) {
            String imageAddress = StringUtils.cleanPath(getOutputFile(image).getAbsolutePath());//.getPath();
            System.out.println(imageAddress); // test
            user.setPhoto(imageAddress);
            userRepository.save(user);
            return new ResponseEntity<>(imageAddress, HttpStatus.OK);
        } else {
            return ResponseEntity.ok(new ResultResponse(false));//("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> getPostProfileMy(MultipartFile photo, String email, String name,
                                              String password, String removePhoto) throws IOException {
        if (!authService.isUserAuthorized()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        boolean result = true;
        User currentUser = userRepository.getOne(authService.getUserId());
        Map<String, Object> errors = new LinkedHashMap<>();
        if(photo != null) {
            int MAX_IMAGE_SIZE = 5_000_000;
            if (photo.getBytes().length <= MAX_IMAGE_SIZE) {
                if (removePhoto.equals("1")) {
                    currentUser.setPhoto("");
                } else {
                    File convertFile = getOutputFile(photo); //аватара форматируется и записывается в папку upload
                    String photoDestination = StringUtils.cleanPath(convertFile.getPath());//getImageAddress(photo);//
                    currentUser.setPhoto("/" + photoDestination);
                    System.out.println("avatarAddress: " + photoDestination);//((ImageOutputStream) image).readLine());
                }
            } else {
                result = false;
                errors.put("photo", "Фото слишком большое, нужно не более 5 Мб.");
            }
        }
        if (password != null) {
            int PW_MIN_LENGTH = 6;
            int PW_MAX_LENGTH = 30;
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
            return new ResponseEntity<>(resultResponse, HttpStatus.BAD_REQUEST);
        } else {
            currentUser.setName(name);
            if(email != null && !currentUser.getEmail().equals(email)) {
                currentUser.setEmail(email);
            }
            if(password != null) {
                currentUser.setPassword(password);
            }
            userRepository.save(currentUser);
            return new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
        }
    }


//    public ResponseEntity<?> getPostProfileMy(ProfileRequest profileRequest) throws IOException {
//        if (!authService.isUserAuthorized()) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        boolean result = true;
//        User currentUser = userRepository.getOne(authService.getUserId());
//        Map<String, Object> errors = new LinkedHashMap<>();
//        if(profileRequest.getPhoto() != null) {
//            int MAX_IMAGE_SIZE = 5_000_000;
//            if (profileRequest.getPhoto().getBytes().length <= MAX_IMAGE_SIZE) {
//                if (profileRequest.getRemovePhoto().equals("1")) {
//                    currentUser.setPhoto("");
//                } else {
//                    File convertFile = getOutputFile(profileRequest.getPhoto()); //аватара форматируется и записывается в папку upload
//                    String photoDestination = StringUtils.cleanPath(convertFile.getPath());//getImageAddress(photo);//
//                    currentUser.setPhoto("/" + photoDestination);
//                    System.out.println("avatarAddress: " + photoDestination);//((ImageOutputStream) image).readLine());
//                }
//            } else {
//                result = false;
//                errors.put("photo", "Фото слишком большое, нужно не более 5 Мб.");
//            }
//        }
//        if (profileRequest.getPassword() != null) {
//            int PW_MIN_LENGTH = 6;
//            int PW_MAX_LENGTH = 30;
//            if (profileRequest.getPassword().length() < PW_MIN_LENGTH && profileRequest.getPassword().length() > PW_MAX_LENGTH) {
//                result = false;
//                errors.put("password", "Длина пароля с ошибкой");
//            }
//        }
//        if (!profileRequest.getName().matches("[a-zA-Z]*") || profileRequest.getName().length() > 100 || profileRequest.getName().length() < 2) {
//            result = false;
//            errors.put("name", "Имя указано неверно.");
//        }
//        if (!result) {
//            return new ResponseEntity<>(resultResponse, HttpStatus.BAD_REQUEST);
//        } else {
//            currentUser.setName(profileRequest.getName());
//            if(profileRequest.getEmail() != null && !currentUser.getEmail().equals(profileRequest.getEmail())) {
//                currentUser.setEmail(profileRequest.getEmail());
//            }
//            if(profileRequest.getPassword() != null) {
//                currentUser.setPassword(profileRequest.getPassword());
//            }
//            userRepository.save(currentUser);
//            return new ResponseEntity<>(new ResultResponse(true), HttpStatus.OK);
//        }
//    }

    private File getOutputFile (MultipartFile photo) throws IOException {
        String targetFolder = "upload/";
        String hashCode = String.valueOf(Math.abs(targetFolder.hashCode()));
        String folder1 = hashCode.substring(0, hashCode.length() / 3);
        String folder2 = hashCode.substring(1 + hashCode.length() / 3, 2 * hashCode.length() / 3);
        String folder3 = hashCode.substring(1 + 2 * hashCode.length() / 3);
        File destFolder = new File(targetFolder);
        if (!destFolder.exists()) {
            destFolder.mkdir();
        }
        File destFolder1 = new File(targetFolder + folder1);
        if (!destFolder1.exists()) {
            destFolder1.mkdir();
        }
        File destFolder2 = new File(targetFolder + folder1 + "/" + folder2);
        if (!destFolder2.exists()) {
            destFolder2.mkdir();
        }
        File destFolder3 = new File(targetFolder + folder1 + "/" + folder2 + "/" + folder3);
        if (!destFolder3.exists()) {
            destFolder3.mkdir();
        }
        int suffix = (int) (Math.random() * 100);
        String fileName = suffix + "_" + photo.getOriginalFilename();
        String finalDestination = targetFolder + folder1 + "/" + folder2 + "/" + folder3 + "/" + fileName;
        photo.transferTo(Path.of(finalDestination));
        File destFile = new File(finalDestination);// Windows separators ("\") are replaced by simple slashes.
        Image image = ImageIO.read(photo.getInputStream());
        int HEIGHT_MAX = 100;
        int WIDTH_MAX = 100;
        BufferedImage tempPNG = resizeImage(image, WIDTH_MAX, HEIGHT_MAX);
        ImageIO.write(tempPNG, "png", destFile);
        return destFile;
    }

    private BufferedImage resizeImage(Image image, int width, int height) {
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

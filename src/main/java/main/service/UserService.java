package main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private AuthService authService;
    private ResponseEntity<?> responseEntity;

    public ResponseEntity<?> postApiImage(@RequestPart("image") MultipartFile image) throws IOException {
        String destination = "upload/";
        String hashCode = String.valueOf(Math.abs(destination.hashCode()));
        String folder1 = hashCode.substring(0, hashCode.length()/3);
        String folder2 = hashCode.substring(1 + hashCode.length()/3, 2*hashCode.length()/3);
        String folder3 = hashCode.substring(1 + 2*hashCode.length()/3);
        if (authService.isUserAuthorized()) {
            try {
                File originalFile = new File(image.getName()); //(Objects.requireNonNull(image.getOriginalFilename()));
                BufferedImage im = ImageIO.read(originalFile);
                int suffix = (int) (Math.random() * 100);
                File destFolder = new File(destination);
                if (!destFolder.exists()) {
                    destFolder.mkdir();
                }
                File destFolder1 = new File(destination + folder1);
                if (!destFolder1.exists()) {
                    destFolder1.mkdir();
                }
                File destFolder2 = new File (destination + folder1 + "/" + folder2);
                if (!destFolder2.exists()) {
                    destFolder2.mkdir();
                }
                String finalDestination = destination + folder1 + "/" + folder2 + "/" + folder3 + "/";
                File destFolder3 = new File (finalDestination);
                if (!destFolder3.exists()) {
                    destFolder3.mkdir();
                }
                String fileName =  suffix + "_uploaded.jpg";
                File output = new File(destFolder3, fileName);
                ImageIO.write(im, "jpg", output);
                responseEntity = new ResponseEntity<>(finalDestination + fileName, HttpStatus.OK);
            } catch (IOException ex) {
                ex.printStackTrace();
                responseEntity = new ResponseEntity<>("No image loaded!", HttpStatus.NOT_FOUND);
            }
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        File convertFile = new File(Objects.requireNonNull(image.getOriginalFilename()));
        convertFile.createNewFile();
        return responseEntity;
    }
}

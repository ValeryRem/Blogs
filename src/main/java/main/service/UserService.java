package main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Service
public class UserService {
    @Autowired
    private AuthService authService;
    private ResponseEntity<?> responseEntity;

    public ResponseEntity<?> postApiImage(@RequestPart("image") MultipartFile image) throws IOException {
        if (authService.isUserAuthorized()) {
            image.transferTo(getOutputFile(image));
            responseEntity = new ResponseEntity<>(getOutputFile(image).getAbsolutePath(), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("User UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    private File getOutputFile (MultipartFile image) {
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

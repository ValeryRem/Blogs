package main.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class AvatarController {
    @GetMapping("/{folder}/{dir1}/{dir2}/{dir3}/{filename}")
    @ResponseBody
    public HttpEntity<byte[]> getPhoto(
            @PathVariable("folder") String folder,
            @PathVariable("dir1") String dir1,
            @PathVariable("dir2") String dir2,
            @PathVariable("dir3") String dir3,
            @PathVariable("filename") String filename) throws IOException {
    String source = folder + "/" + dir1 + "/" + dir2 + "/" + dir3 + "/" + filename;
        byte[] image = org.apache.commons.io.FileUtils.readFileToByteArray(new File(source));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(image.length);
        return new HttpEntity<>(image, headers);
    }
}

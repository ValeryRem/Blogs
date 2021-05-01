package main.controllers;

import main.service.GetService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/{folder}/{dir1}/{dir2}/{dir3}/{filename}")
public class AvatarController {

    private final GetService getService;

    public AvatarController(GetService getService) {
        this.getService = getService;
    }

    @GetMapping("")
    @ResponseBody
    private HttpEntity<byte[]> getPhoto(
            @PathVariable("folder") String folder,
            @PathVariable("dir1") String dir1,
            @PathVariable("dir2") String dir2,
            @PathVariable("dir3") String dir3,
            @PathVariable("filename") String filename) throws IOException {
        return getService.getPhoto(folder, dir1, dir2, dir3, filename);
    }
}

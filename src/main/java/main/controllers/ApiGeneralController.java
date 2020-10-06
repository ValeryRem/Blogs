package main.controllers;

import main.api.response.InitResponse;
import main.api.response.PostResponse;
import main.api.response.SettingsResponse;
import main.service.SettingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final SettingsService settingsService;
    private final InitResponse initResponse;
    private final PostResponse postResponse;

    public ApiGeneralController(SettingsService settingsService, InitResponse initResponse, PostResponse postResponse  ) {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
        this.postResponse = postResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/post")
    private PostResponse post() {
        return postResponse;
    }
}

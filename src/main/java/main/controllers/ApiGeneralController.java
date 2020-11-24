package main.controllers;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.service.GetService;
import main.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final SettingsService settingsService;
    private final InitResponse initResponse;
    @Autowired
    private GetService getService;


    public ApiGeneralController(SettingsService settingsService, InitResponse initResponse) {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/init")
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping("/tag")
    private ResponseEntity<?> getTag (@RequestParam(defaultValue = "#PHP #Spring #Java")  String query) {
        return getService.getTag(query);
    }

    @GetMapping("/statistics/my")
    private ResponseEntity <?> getMyStatistics (@RequestParam(defaultValue = "1") Integer userId) {
        return getService.getMyStatistics(userId);
    }
}

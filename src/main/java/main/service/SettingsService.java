package main.service;

import main.response.ResultResponse;
import main.entity.GlobalSettings;
import main.repository.GlobalSettingsRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class SettingsService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final GlobalSettingsRepository globalSettingsRepository;
    private GlobalSettings globalSettings;

    ResponseEntity<?> responseEntity;

    public SettingsService(AuthService authService, UserRepository userRepository,
                           GlobalSettingsRepository globalSettingsRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.globalSettingsRepository = globalSettingsRepository;
    }

    public ResponseEntity<?> putApiSettings (boolean multiuserMode, boolean postPremoderation, boolean statisticsIsPublic) {
        Integer userId = authService.getUserId();

        globalSettings = new GlobalSettings();
        if(authService.isUserAuthorized() && userRepository.getOne(userId).getIsModerator()) {
            if (areSettingsExist()) {
                globalSettingsRepository.deleteAll();
            } else {
                globalSettings.setMultiuserMode(multiuserMode);
                globalSettings.setPostPremoderation(postPremoderation);
                globalSettings.setStatisticsIsPublic(statisticsIsPublic);
                globalSettingsRepository.save(globalSettings);
                Map<String, Boolean> map = getBooleanMap();
                responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
            }
        } else {
            responseEntity = new ResponseEntity<>(new ResultResponse(false), HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    public ResponseEntity<?> getApiSettings () {
        Map<String, Boolean> map = getBooleanMap();
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    private Map<String, Boolean> getBooleanMap() {
        Map<String, Boolean> map = new LinkedHashMap<>();
        globalSettings = globalSettingsRepository.findAll().stream().findFirst().orElse(new GlobalSettings());
        map.put("MULTIUSER_MODE", globalSettings.isMultiuserMode());
        map.put("POST_PREMODERATION", globalSettings.isPostPremoderation());
        map.put("STATISTICS_IS_PUBLIC", globalSettings.isStatisticsIsPublic());
        return map;
    }

    public boolean areSettingsExist() {
        return globalSettingsRepository.count() > 0;
    }
}

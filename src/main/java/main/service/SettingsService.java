package main.service;

import main.api.response.SettingsResponse;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    private boolean settingsExist;

    @Autowired
    SettingsResponse settingsResponse;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    ResponseEntity<?> responseEntity;

    public SettingsResponse getGlobalSettings (){
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(true);
        settingsResponse.setPostPremoderation(true);
        settingsResponse.setStatisticsIsPublic(true);
        settingsExist = true;
        return settingsResponse;
    }

    public ResponseEntity<?> putApiSettings (boolean multiuserMode, boolean postPremoderation, boolean statisticsInPublic ) {
        Integer userId = authService.getUserId();

        if(authService.isUserAuthorized() && userRepository.getOne(userId).getIsModerator()) {
            settingsResponse.setStatisticsIsPublic(statisticsInPublic);
            settingsResponse.setPostPremoderation(postPremoderation);
            settingsResponse.setMultiuserMode(multiuserMode);
            responseEntity = new ResponseEntity<>(settingsResponse, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    public boolean areSettingsExist() {
        return settingsExist;
    }
}

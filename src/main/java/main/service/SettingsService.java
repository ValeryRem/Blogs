package main.service;

import main.api.response.SettingsResponse;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class SettingsService {
    private boolean settingsExist;

    @Autowired
    HttpSession httpSession;

    @Autowired
    SettingsResponse settingsResponse;

    @Autowired
    AuthSevice authSevice;

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
        String sessionId = httpSession.getId();
        Integer userId = authSevice.getSessionMap().get(sessionId);

        if(authSevice.isUserAuthorized(sessionId) && userRepository.getOne(userId).getIsModerator()) {
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

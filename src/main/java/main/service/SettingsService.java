package main.service;

import main.api.response.SettingsResponse;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    private boolean settingsExist;

    public SettingsResponse getGlobalSettings (){
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(true);
        settingsResponse.setPostPremoderation(true);
        settingsResponse.setStatisticsIsPublic(true);
        settingsExist = true;
        return settingsResponse;
    }

    public boolean areSettingsExist() {
        return settingsExist;
    }
}

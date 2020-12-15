package main.api.response;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ErrorsResponse {
    Map<String, Object> responseMap = new LinkedHashMap<>();

    public ErrorsResponse() {
        responseMap.put("result", false);
    }
    public ErrorsResponse(Map<String, Object> map) {
        responseMap.put("result", false);
        responseMap.put("errors", map);
    }

    public Map<String, Object> getResponseMap() {
        return responseMap;
    }

    public void setResponseMap(Map<String, Object> responseMap) {
        this.responseMap = responseMap;
    }
}

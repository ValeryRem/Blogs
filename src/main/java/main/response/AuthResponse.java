package main.response;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.TreeMap;


public class AuthResponse {
    private boolean result  ;
    private LinkedHashMap<String, Object> user;

    public AuthResponse() {
    }

    public AuthResponse(boolean result, LinkedHashMap<String, Object> user) {
        this.result = result;
        this.user = user;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public LinkedHashMap<String, Object> getUser() {
        return user;
    }

    public void setUser(LinkedHashMap<String, Object> user) {
        this.user = user;
    }
}

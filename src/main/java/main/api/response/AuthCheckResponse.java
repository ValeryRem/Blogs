package main.api.response;

import java.util.TreeMap;

public class AuthCheckResponse {
    private boolean result;
    private TreeMap<String, Object> user;

    public AuthCheckResponse(boolean result, TreeMap<String, Object> user) {
        this.result = result;
        this.user = user;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public TreeMap<String, Object> getUser() {
        return user;
    }

    public void setUser(TreeMap<String, Object> user) {
        this.user = user;
    }
}

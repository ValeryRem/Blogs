package main.entity;

import java.util.Map;
import java.util.TreeMap;

public class Session {
    Map<String, Integer> session;

    public Session() {
    }

    public Session(String timeStamp, Integer userId) {
        this.session = new TreeMap<>();
        session.put(timeStamp, userId);
    }

    public Map<String, Integer> getSession() {
        return session;
    }

    public void setSession(Map<String, Integer> session) {
        this.session = session;
    }
}

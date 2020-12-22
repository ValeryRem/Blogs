package main.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer sessionId;
    String sessionName;
    Timestamp time;
    Integer userId;

    public Session() {
    }

    public Session(String sessionName, Timestamp time, Integer userId) {
        this.sessionName = sessionName;
        this.time = time;
        this.userId = userId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

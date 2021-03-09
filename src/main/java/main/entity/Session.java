package main.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer sessionId;
    String sessionName;
    Timestamp timestamp;
    Integer userId;

    public Session() {
    }

    public Session(String sessionName, Timestamp timestamp, Integer userId) {
        this.sessionName = sessionName;
        this.timestamp = timestamp;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

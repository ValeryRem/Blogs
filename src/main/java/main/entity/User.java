package main.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    @JoinTable(name = "posts", joinColumns = @JoinColumn(name = "post_id"))
    private Integer userId;

    @Column(name = "is_moderator")
    private Integer isModerator;

    @Column(name = "reg_time")
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate regTime;

    @Column(name = "user_name")
    @JoinTable(name = "posts", joinColumns = @JoinColumn(name = "post_id"))
    private String name;
    private String email;
    private String password;
    private String code;
    private URL photo;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(Integer isModerator) {
        this.isModerator = isModerator;
    }

    public LocalDate getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDate regTime) {
        this.regTime = regTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public URL getPhoto() {
        return photo;
    }

    public void setPhoto(URL photo) {
        this.photo = photo;
    }
}

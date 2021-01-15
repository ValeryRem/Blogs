package main.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JoinTable(name = "posts", joinColumns = @JoinColumn(name = "post_id"))
    private Integer userId;

    @Column(name = "is_moderator")
    private boolean isModerator;

    @Column(name = "reg_time")
//    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private Timestamp regTime;

    @Column(name = "user_name")
    @JoinTable(name = "posts", joinColumns = @JoinColumn(name = "post_id"))
    private String name;
    @Email
    @JsonProperty("e_mail")
    @Column(name = "e_mail")
    private String eMail;
    @Size(min=6, max=20, message = "Password to be between 6 & 20 chars' number")
    private String password;
    private String code;
    private String photo;

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

    public boolean getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(boolean isModerator) {
        this.isModerator = isModerator;
    }

    public Timestamp getRegTime() {
        return regTime;
    }

    public void setRegTime(Timestamp regTime) {
        this.regTime = regTime;
    }

    public String getEmail() {
        return eMail;
    }

    public void setEmail(String email) {
        this.eMail = email;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

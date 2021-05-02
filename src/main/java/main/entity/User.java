package main.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
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
    @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private Timestamp regTime;

    @JoinTable(name = "posts", joinColumns = @JoinColumn(name = "post_id"))
    private String name;

    @Email
    @Column(name = "e_mail", unique = true)
    private String email;

//    @Size(min=6, max=20, message = "Password to be between 6 & 20 chars' number")
    private String password;

    private String code;

    @Nullable
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


    public void setRegTime( @DateTimeFormat(pattern = "YYYY-MM-dd HH:mm:ss") Timestamp regTime) {
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

    @Nullable
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(@Nullable String photo) {
        this.photo = photo;
    }
}

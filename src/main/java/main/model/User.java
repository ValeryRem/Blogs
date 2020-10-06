package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @OneToOne(mappedBy = "post_id")// Caused by: org.hibernate.AnnotationException: Illegal attempt to map a non collection as a @OneToMany, @ManyToMany or @CollectionOfElements: main.model.User.id
    private Integer id;
    @NotNull(message = "Name is mandatory")
    private String name;
    @NotNull(message = "Status is mandatory")
    @Column(name = "is_moderator")
    private Integer isModerator;
    @Column(name = "reg_time")
    @NotNull(message = "regTime is mandatory")
    private Date regTime;
    @NotNull(message = "email is mandatory")
    private String email;
    @NotNull(message = "password is mandatory")
    private String password;
    private String code;
    private String photo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

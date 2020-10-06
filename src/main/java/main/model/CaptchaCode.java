package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "capture_codes")
public class CaptchaCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Time of release is mandatory")
    private Date time;
    @NotBlank(message = "Capture code is mandatory")
    private Integer code;
    @NotBlank(message = "Secret code is mandatory")
    @Column(name = "secret_code")
    private Integer secretCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(Integer secretCode) {
        this.secretCode = secretCode;
    }
}

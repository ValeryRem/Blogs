package main.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Service
public class ProfileRequest implements Serializable {
    @Nullable
    private MultipartFile photo;
    private String name;
    private String email;
    private String password;
    private String removePhoto;

    @Nullable
    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(@Nullable MultipartFile photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRemovePhoto() {
        return removePhoto;
    }

    public void setRemovePhoto(String removePhoto) {
        this.removePhoto = removePhoto;
    }
}

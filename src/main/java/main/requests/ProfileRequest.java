package main.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class ProfileRequest implements Serializable {
//    @JsonProperty
//    private String photoAddress;

    private MultipartFile photo;

    @JsonProperty
    private String name;

    @JsonProperty
    private String email;

    @JsonProperty("e_mail")
    private String password;

    @JsonProperty ("remove_photo")
    private String removePhoto;

//    public String getPhotoAddress() {
//        return photoAddress;
//    }
//
//    public void setPhotoAddress(String photoAddress) {
//        this.photoAddress = photoAddress;
//    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
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

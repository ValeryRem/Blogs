package main.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.entity.Post;

public class UserResponse {
    @JsonProperty
    private Integer id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String photo;
//    private User user = new User(userId);


    public UserResponse() {
    }

    public UserResponse(Post post) {
    }

    public UserResponse(Integer id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

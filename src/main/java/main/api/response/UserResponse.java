package main.api.response;

import main.entity.User;

public class UserResponse {
    private Integer userId;
    private User user = new User(userId);

    public User getUser(Integer userId) {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }
}

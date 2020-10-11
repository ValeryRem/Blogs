package main.api.response;

import main.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponse {
    private Integer userId;
    private User user = new User(userId);

    public User getUser(Integer userId) {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

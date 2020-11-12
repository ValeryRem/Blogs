package main.entity;

import javax.persistence.*;
import java.awt.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "is_moderator")
    private Integer isModerator;

    @Column(name = "reg_time")
    private Date regTime;
    private String email;
    private String password;
    private String code;
    private URL photo;
    private TreeMap<String, Object> userSelect;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> postList;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("id", getUserId());
        map.put("name", getName());
        map.put("photo", getPhoto());
        userSelect = map;
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

    public URL getPhoto() {
        return photo;
    }

    public void setPhoto(URL photo) {
        this.photo = photo;
    }

    public TreeMap<String, Object> getUserSelect() {
        return userSelect;
    }

    public void setUserSelect(TreeMap<String, Object> userSelect) {
        this.userSelect = userSelect;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }
}

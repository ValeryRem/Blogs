package main.model;

import java.util.List;

public class PostList {
    private List<Post> posts;
    private Integer count;

    public PostList() {
    }

    public PostList(Integer count, List<Post> posts) {
        this.count = count;
        this.posts = posts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

package main.api.response;

import java.util.List;

public class GeneralResponse {
    private int count;
    private List<Object> posts;

    public GeneralResponse() {
    }

    public GeneralResponse(int count, List<Object> posts) {
        this.count = count;
        this.posts = posts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Object> getPosts() {
        return posts;
    }

    public void setPosts(List<Object> list) {
        this.posts = list;
    }
}

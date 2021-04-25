package main.response;

import java.util.List;
import java.util.Map;

public class GeneralResponse {
    private int count;
    private List<Map<String, Object>> posts;

    public GeneralResponse() {
    }

    public GeneralResponse(int count, List<Map<String, Object>> posts) {
        this.count = count;
        this.posts = posts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Map<String, Object>> getPosts() {
        return posts;
    }

    public void setPosts(List<Map<String, Object>> list) {
        this.posts = list;
    }
}

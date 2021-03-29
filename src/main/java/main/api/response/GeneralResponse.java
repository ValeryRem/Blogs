package main.api.response;

import java.util.List;

public class GeneralResponse {
    private int count;
    private List<PostResponse> posts;

    public GeneralResponse() {
    }

    public GeneralResponse(int count, List<PostResponse> posts) {
        this.count = count;
        this.posts = posts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> list) {
        this.posts = list;
    }
}

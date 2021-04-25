package main.response;

import java.util.List;

public class PostsForModerationResponse {
    private Integer count;
    private List<PostResponse> posts;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }
}

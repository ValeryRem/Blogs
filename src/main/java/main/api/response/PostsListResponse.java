package main.api.response;

import java.util.List;

public class PostsListResponse {
    int count;
    List<PostResponse> posts;

    public PostsListResponse() {
    }

    public PostsListResponse(int count, List<PostResponse> posts) {
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
}


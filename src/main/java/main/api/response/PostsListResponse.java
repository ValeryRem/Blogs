package main.api.response;

import java.util.List;

public class PostsListResponse {
    int count;
    List<PostAnnounceResponse> posts;

    public PostsListResponse() {
    }

    public PostsListResponse(int count, List<PostAnnounceResponse> list) {
        this.count = count;
        this.posts = list;
    }

    public int getCount() {
        return posts.size();
    }

    public void setCount(int count) {

        this.count = count;
    }

    public List<PostAnnounceResponse> getList() {
        return posts;
    }
}

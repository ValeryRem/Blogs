package main.api.response;

import org.hibernate.engine.transaction.jta.platform.internal.JBossAppServerJtaPlatform;

import java.util.List;

public class PostsListResponse {
    int count;
    List<PostAnnounceResponse> posts;

    public PostsListResponse() {
    }

    public PostsListResponse(int count, List<PostAnnounceResponse> posts) {
        this.count = count;
        this.posts = posts;
    }

    public int getCount() {
        return posts.size();
    }

    public void setCount(int count) {

        this.count = count;
    }

    public List<PostAnnounceResponse> getPosts() {
        return posts;
    }
}

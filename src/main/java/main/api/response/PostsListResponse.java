package main.api.response;

import java.util.List;

public class PostsListResponse {
    private final int count; // удобно сделать поля final, чтобы не подпортить после создания
    private final List<PostPreviewResponse> posts;
    public PostsListResponse(int count, List<PostPreviewResponse> posts) {
        this.count = count;
        this.posts = posts;
    }

    public int getCount() {
        return count;
    }

    public List<PostPreviewResponse> getPosts() {
        return posts;
    }
}

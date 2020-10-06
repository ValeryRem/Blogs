package main.api.response;

import main.base.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PostResponse {
    @Autowired
    private Storage storage;
    private final List<PostPreviewResponse> posts;
    public int count; // удобно сделать поля final, чтобы не подпортить после создания


    public PostResponse(List<PostPreviewResponse> posts) {
        this.posts = posts;
    }

    public int getCount() {
        return  storage.getPosts().size();
    }

    public List<PostPreviewResponse> getPosts() {
        return posts;
    }
}

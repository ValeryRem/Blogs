package main.api.response;

import java.util.List;

public class PostsListResponse {
    int count;
    List<PostAnnounceResponse> list;

    public PostsListResponse() {
    }

    public PostsListResponse(int count, List<PostAnnounceResponse> list) {
        this.count = count;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostAnnounceResponse> getList() {
        return list;
    }
}

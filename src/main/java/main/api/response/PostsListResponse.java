package main.api.response;

import java.util.List;

public class PostsListResponse {
    int count;
    List<PostAnnounceResponse> postAnnounceResponseList;

    public PostsListResponse() {
    }

    public PostsListResponse(int count, List<PostAnnounceResponse> postAnnounceResponseList) {
        this.count = count;
        this.postAnnounceResponseList = postAnnounceResponseList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostAnnounceResponse> getPostAnnounceResponseList() {
        return postAnnounceResponseList;
    }
}


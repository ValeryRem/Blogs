package main.api.response;

import java.util.List;

public class PostsListResponse {
    private final int count; // удобно сделать поля final, чтобы не подпортить после создания
    private final List<PostPreviewResponse> posts;
//"id": 345,
//        "timestamp": 1592338706,
//        "user":
//    {
//        "id": 88,
//            "name": "Дмитрий Петров"
//    },
//            "title": "Заголовок поста",
//            "announce": "Текст анонса поста без HTML-тэгов",
//            "likeCount": 36,
//            "dislikeCount": 3,
//            "commentCount": 15,
//            "viewCount": 55
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

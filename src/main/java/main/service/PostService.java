package main.service;

import main.api.response.PostResponse;
import main.api.response.PostsListResponse;
import main.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    public List<Post> posts;
    private Integer offset;// - сдвиг от 0 для постраничного вывода
    private Integer limit;// - количество постов, которое надо вывести
    private Integer mode;
    private boolean recent;
    private boolean popular;
    private boolean best;
    private boolean early;

    @Autowired
    private PostResponse postResponse;
//    @Autowired
    private PostsListResponse postsListResponse;

    public ResponseEntity<?> getPosts () {
         return postResponse.getPosts(offset, limit);
    }

    public ResponseEntity<Post> getPostById (Integer postId) {
        return postResponse.getPostById(postId);
    }
    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getMode() {
        // - режим вывода (сортировка):
        return mode;
    }

    public boolean isRecent() {
        // - сортировать по дате публикации, выводить сначала новые
       return recent;
    }

    public boolean isPopular() {
        // - сортировать по убыванию количества комментариев
        return popular;
    }

    public boolean isBest() {
        // - сортировать по убыванию количества лайков
        return best;
    }

    public boolean isEarly() {
        // - сортировать по дате публикации, выводить сначала старые
        return early;
    }
}

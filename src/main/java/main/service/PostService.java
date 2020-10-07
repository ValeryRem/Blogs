package main.service;

import main.base.Storage;
import main.model.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private Storage storage;
    public List<Post> posts;
//    @Autowired
//    private PostRepository postRepository;
    private Integer offset = 0;// - сдвиг от 0 для постраничного вывода
    private Integer limit = 5;// - количество постов, которое надо вывести
    private Integer mode = 1;// - режим вывода (сортировка):
    private boolean recent = true;// - сортировать по дате публикации, выводить сначала новые
    private boolean popular = false;// - сортировать по убыванию количества комментариев
    private boolean best = true;// - сортировать по убыванию количества лайков
    private boolean early = false;// - сортировать по дате публикации, выводить сначала старые

//    public PostRepository getPostRepository() {
//        return postRepository;
//    }
    public List<Post> getPosts(){
        return storage.getPosts();
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getMode() {
        return mode;
    }

    public boolean isRecent() {
        return recent;
    }

    public boolean isPopular() {
        return popular;
    }

    public boolean isBest() {
        return best;
    }

    public boolean isEarly() {
        return early;
    }
}

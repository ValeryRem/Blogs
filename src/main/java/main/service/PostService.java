package main.service;

import main.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final int offset;// - сдвиг от 0 для постраничного вывода
    private final int limit;// - количество постов, которое надо вывести
    private final int mode;// - режим вывода (сортировка):
    private final boolean recent;// - сортировать по дате публикации, выводить сначала новые
    private final boolean popular;// - сортировать по убыванию количества комментариев
    private final boolean best;// - сортировать по убыванию количества лайков
    private final boolean early;// - сортировать по дате публикации, выводить сначала старые

    public PostService(PostRepository postRepository, int offset, int limit, int mode, boolean recent, boolean popular, boolean best, boolean early) {
        this.postRepository = postRepository;
        this.offset = offset;
        this.limit = limit;
        this.mode = mode;
        this.recent = recent;
        this.popular = popular;
        this.best = best;
        this.early = early;
    }

    public PostRepository getPostRepository() {
        return postRepository;
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

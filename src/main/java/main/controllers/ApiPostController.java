package main.controllers;

import main.model.Post;
import main.repository.PostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/post")
public class ApiPostController {
    private final PostRepository postRepository;
    private int offset;// - сдвиг от 0 для постраничного вывода
    private int limit;// - количество постов, которое надо вывести
    private int mode;// - режим вывода (сортировка):
    private boolean recent;// - сортировать по дате публикации, выводить сначала новые
    private boolean popular;// - сортировать по убыванию количества комментариев
    private boolean best;// - сортировать по убыванию количества лайков
    private boolean early;// - сортировать по дате публикации, выводить сначала старые

    public ApiPostController(PostRepository postRepository, int offset, int limit, int mode, boolean recent,
                             boolean popular, boolean best, boolean early) {
        this.postRepository = postRepository;
        this.offset = offset;
        this.limit = limit;
        this.mode = mode;
        this.recent = recent;
        this.popular = popular;
        this.best = best;
        this.early = early;
    }

    @GetMapping("/")
    private ResponseEntity<?> getPosts(int offset, int limit) {
        Iterable<Post> posts = postRepository.findAll();
        List<Post> result = new ArrayList<>();
        for (Post post : posts) {
            result.add(post);
        }
        return ResponseEntity.ok(result.subList(offset, limit));
    }
}

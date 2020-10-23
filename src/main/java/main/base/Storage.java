package main.base;

import main.entity.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class Storage{

    private final List<Post> posts = new ArrayList<>();

    @Autowired
    private PostRepository postRepository;

    @Autowired
    public Storage storage;

    public Storage(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public void addPost (Post post) {
        Iterable<Post> postIterable = postRepository.findAll();
        posts.add(post);
        postIterable.forEach(posts::add);
        postRepository.save(post);
    }

    public List<Post> getPosts() {
        return posts;
    }
}
